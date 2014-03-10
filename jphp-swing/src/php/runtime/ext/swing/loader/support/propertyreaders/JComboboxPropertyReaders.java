package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JComboboxPropertyReaders extends PropertyReaders<JComboBox> {
    protected final Map<String, PropertyReader<JComboBox>> register = new HashMap<String, PropertyReader<JComboBox>>(){{
        put("items", ITEMS);
        put("max-row-count", MAX_ROW_COUNT);
        put("selected-index", SELECTED_INDEX);
        put("readonly", READONLY);
    }};

    @Override
    protected Map<String, PropertyReader<JComboBox>> getRegister() {
        return register;
    }

    @Override
    public Class<JComboBox> getRegisterClass() {
        return JComboBox.class;
    }

    public final static PropertyReader<JComboBox> MAX_ROW_COUNT = new PropertyReader<JComboBox>() {
        @Override
        public void read(JComboBox component, Value value) {
            component.setMaximumRowCount(value.asInteger());
        }
    };

    public final static PropertyReader<JComboBox> SELECTED_INDEX = new PropertyReader<JComboBox>() {
        @Override
        public void read(JComboBox component, Value value) {
            component.setSelectedIndex(value.asInteger());
        }
    };

    public final static PropertyReader<JComboBox> READONLY = new PropertyReader<JComboBox>() {
        @Override
        public void read(JComboBox component, Value value) {
            component.setEditable(!value.asBoolean());
        }
    };

    public final static PropertyReader<JComboBox> ITEMS = new PropertyReader<JComboBox>() {
        @Override
        public void read(JComboBox component, Value value) {
            component.setModel(new DefaultComboBoxModel<String>(value.asArray(false)));
        }
    };
}
