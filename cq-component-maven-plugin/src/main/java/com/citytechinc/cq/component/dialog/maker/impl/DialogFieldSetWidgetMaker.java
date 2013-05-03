package com.citytechinc.cq.component.dialog.maker.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.dialog.DialogElement;
import com.citytechinc.cq.component.dialog.exception.InvalidComponentFieldException;
import com.citytechinc.cq.component.dialog.factory.WidgetFactory;
import com.citytechinc.cq.component.dialog.impl.DialogFieldSetWidget;
import com.citytechinc.cq.component.dialog.impl.WidgetCollection;
import com.citytechinc.cq.component.dialog.maker.AbstractWidgetMaker;
import com.citytechinc.cq.component.dialog.maker.WidgetMaker;
import com.citytechinc.cq.component.maven.util.ComponentMojoUtil;
import com.citytechinc.cq.component.maven.util.WidgetConfigHolder;

public class DialogFieldSetWidgetMaker extends AbstractWidgetMaker {
	private static final String ITEMS = "items";

	@Override
	public DialogElement make(String xtype, Field widgetField, CtField ctWidgetField, Class<?> containingClass,
		CtClass ctContainingClass, Map<Class<?>, WidgetConfigHolder> xtypeMap,
		Map<String, WidgetMaker> xTypeToWidgetMakerMap, ClassLoader classLoader, ClassPool classPool,
		boolean useDotSlashInName) throws ClassNotFoundException, InvalidComponentFieldException,
		CannotCompileException, NotFoundException, SecurityException, NoSuchFieldException {

		DialogFieldSet dialogFieldSetAnnotation = (DialogFieldSet) ctWidgetField.getAnnotation(DialogFieldSet.class);
		DialogField dialogFieldAnnotation = (DialogField) ctWidgetField.getAnnotation(DialogField.class);

		boolean collapseFirst = dialogFieldSetAnnotation.collapseFirst();
		boolean collapsible = dialogFieldSetAnnotation.collapsible();
		boolean collapsed = dialogFieldSetAnnotation.collapsed();
		boolean border = dialogFieldSetAnnotation.border();
		String title = null;
		if (!StringUtils.isEmpty(dialogFieldSetAnnotation.title())) {
			title = dialogFieldSetAnnotation.title();
		}

		String fieldName = getFieldNameForField(dialogFieldAnnotation, widgetField);
		String fieldLabel = getFieldLabelForField(dialogFieldAnnotation, widgetField);
		String fieldDescription = getFieldDescriptionForField(dialogFieldAnnotation);
		Map<String, String> additionalProperties = getAdditionalPropertiesForField(dialogFieldAnnotation);
		boolean hideLabel = dialogFieldAnnotation.hideLabel();

		List<DialogElement> widgetCollection = buildWidgetCollection(ctContainingClass, ctWidgetField, widgetField,
			xtypeMap, xTypeToWidgetMakerMap, classLoader, classPool);

		return new DialogFieldSetWidget(collapseFirst, collapsible, collapsed, border, title, fieldLabel, fieldDescription,
			hideLabel, fieldName, additionalProperties, widgetCollection);
	}

	private List<DialogElement> buildWidgetCollection(CtClass componentClass, CtField curField, Field trueField,
		Map<Class<?>, WidgetConfigHolder> classToXTypeMap, Map<String, WidgetMaker> xTypeToWidgetMakerMap,
		ClassLoader classLoader, ClassPool classPool) throws InvalidComponentFieldException, ClassNotFoundException,
		CannotCompileException, NotFoundException, SecurityException, NoSuchFieldException {
		CtClass clazz = curField.getType();
		CtField[] fields = ComponentMojoUtil.collectFields(clazz).toArray(new CtField[0]);
		List<DialogElement> elements = new ArrayList<DialogElement>();
		for (CtField field : fields) {
			if (field.hasAnnotation(DialogField.class)) {
				Field mcTrueField = classLoader.loadClass(field.getDeclaringClass().getName()).getDeclaredField(
					field.getName());
				DialogElement builtFieldWidget = WidgetFactory.make(componentClass, field, mcTrueField,
					classToXTypeMap, xTypeToWidgetMakerMap, classLoader, classPool, false, -1);
				elements.add(builtFieldWidget);
			}
		}
		return Arrays.asList(new DialogElement[] { new WidgetCollection(elements, ITEMS) });
	}

}