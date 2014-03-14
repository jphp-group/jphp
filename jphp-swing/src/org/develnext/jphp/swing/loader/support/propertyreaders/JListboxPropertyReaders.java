package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;
import org.develnext.jphp.swing.support.JListbox;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JListboxPropertyReaders extends PropertyReaders<JListbox> {
    protected final Map<String, PropertyReader<JListbox>> register = new HashMap<String, PropertyReader<JListbox>>(){{
        put("items", ITEMS);
        put("selected-index", SELECTED_INDEX);
        put("multiple", MULTIPLE);
    }};

    @Override
    protected Map<String, PropertyReader<JListbox>> getRegister() {
        return register;
    }

    @Override
    public Class<JListbox> getRegisterClass() {
        return JListbox.class;
    }

    public final static PropertyReader<JListbox> ITEMS = new PropertyReader<JListbox>() {
        @Override
        public void read(JListbox component, Value value) {
            DefaultListModel model = (DefaultListModel) component.getModel();
            model.clear();
            for(String el : value.asArray(false)) {
                model.addElement(el);
            }
        }
    };

    public final static PropertyReader<JListbox> SELECTED_INDEX = new PropertyReader<JListbox>() {
        @Override
        public void read(JListbox component, Value value) {
            component.setSelectedIndex(value.asInteger());
        }
    };

    public final static PropertyReader<JListbox> MULTIPLE = new PropertyReader<JListbox>() {
        @Override
        public void read(JListbox component, Value value) {
            component.setMultiple(value.asBoolean());
        }
    };

}
