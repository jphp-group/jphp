package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.text.JTextComponent;
import java.util.HashMap;
import java.util.Map;

public class JTextComponentPropertyReaders extends PropertyReaders<JTextComponent> {

    protected final Map<String, PropertyReader<JTextComponent>> register = new HashMap<String, PropertyReader<JTextComponent>>(){{
        put("text", TEXT);
        put("caret-position", CARET_POSITION);
        put("caret-color", CARET_COLOR);
        put("readonly", READONLY);
        put("selection-color", SELECTION_COLOR);
        put("selected-text-color", SELECTED_TEXT_COLOR);
        put("disabled-text-color", DISABLED_TEXT_COLOR);
    }};

    @Override
    protected Map<String, PropertyReader<JTextComponent>> getRegister() {
        return register;
    }

    @Override
    public Class<JTextComponent> getRegisterClass() {
        return JTextComponent.class;
    }

    public final static PropertyReader<JTextComponent> TEXT = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setText(value.asString());
        }

        @Override
        public boolean isTranslatable() {
            return true;
        }
    };

    public final static PropertyReader<JTextComponent> CARET_POSITION = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setCaretPosition(value.asInteger());
        }
    };

    public final static PropertyReader<JTextComponent> CARET_COLOR = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setCaretColor(value.asColor());
        }
    };

    public final static PropertyReader<JTextComponent> READONLY = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setEditable(!value.asBoolean());
        }
    };

    public final static PropertyReader<JTextComponent> SELECTION_COLOR = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setSelectionColor(value.asColor());
        }
    };

    public final static PropertyReader<JTextComponent> SELECTED_TEXT_COLOR = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setSelectedTextColor(value.asColor());
        }
    };

    public final static PropertyReader<JTextComponent> DISABLED_TEXT_COLOR = new PropertyReader<JTextComponent>() {
        @Override
        public void read(JTextComponent component, Value value) {
            component.setDisabledTextColor(value.asColor());
        }
    };
}
