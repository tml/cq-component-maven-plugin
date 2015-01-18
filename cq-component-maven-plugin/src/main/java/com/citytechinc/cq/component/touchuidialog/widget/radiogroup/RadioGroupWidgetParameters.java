/**
 *    Copyright 2013 CITYTECH, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.citytechinc.cq.component.touchuidialog.widget.radiogroup;

import com.citytechinc.cq.component.touchuidialog.widget.TouchUIWidgetParameters;
import com.citytechinc.cq.component.touchuidialog.widget.datasource.DataSource;
import com.citytechinc.cq.component.touchuidialog.widget.selection.options.Option;
import com.citytechinc.cq.component.touchuidialog.widget.selection.options.Options;
import com.citytechinc.cq.component.touchuidialog.widget.selection.options.OptionsParameters;
import com.citytechinc.cq.component.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class RadioGroupWidgetParameters extends TouchUIWidgetParameters {

    private List<Option> options;
    private DataSource dataSource;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Options getOptions() {
        if (options != null) {
            OptionsParameters optionsParameters = new OptionsParameters();
            optionsParameters.setOptions(options);
            return new Options(optionsParameters);
        }

        return null;
    }

    public void addOption(Option option) {
        if (options == null) {
            options = new ArrayList<Option>();
        }

        options.add(option);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<? extends XmlElement> getContainedElements() {
        List<XmlElement> allContainedElements = new ArrayList<XmlElement>();

        if (getOptions() != null) {
            allContainedElements.add(getOptions());
        }

        if (getDataSource() != null) {
            allContainedElements.add(getDataSource());
        }

        if (containedElements != null) {
            allContainedElements.addAll(containedElements);
        }

        return allContainedElements;
    }
}