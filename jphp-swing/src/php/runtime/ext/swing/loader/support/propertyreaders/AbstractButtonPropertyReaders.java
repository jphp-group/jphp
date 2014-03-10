package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.ComponentProperties;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class AbstractButtonPropertyReaders extends PropertyReaders<AbstractButton> {

    protected final Map<String, PropertyReader<AbstractButton>> register = new HashMap<String, PropertyReader<AbstractButton>>(){{
        put("text", TEXT);
        put("selected", SELECTED);
        put("icon", ICON);
        put("disabled-icon", DISABLED_ICON);
        put("selected-icon", SELECTED_ICON);
        put("pressed-icon", PRESSED_ICON);
        put("icon-text-gap", ICON_TEXT_GAP);
        put("rollover-icon", ROLLOVER_ICON);
        put("rollover-selected-icon", ROLLOVER_SELECTED_ICON);
        put("disabled-selected-icon", DISABLED_SELECTED_ICON);
        put("border-painted", BORDER_PAINTED);
        put("focus-painted", FOCUS_PAINTED);
        put("rollover-enabled", ROLLOVER_ENABLED);
        put("content-area-filled", CONTENT_AREA_FILLED);
        put("button-group", BUTTON_GROUP);
    }};

    @Override
    protected Map<String, PropertyReader<AbstractButton>> getRegister() {
        return register;
    }

    @Override
    public Class<AbstractButton> getRegisterClass() {
        return AbstractButton.class;
    }

    public final static PropertyReader<AbstractButton> BUTTON_GROUP = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            ComponentProperties properties = SwingExtension.getProperties(component);
            properties.setData("buttonGroup", value.asString());

            ButtonGroup group = SwingExtension.getOrCreateButtonGroup(value.asString());
            group.add(component);
        }
    };

    public final static PropertyReader<AbstractButton> TEXT = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setText(value.asString());
        }

        @Override
        public boolean isTranslatable() {
            return true;
        }
    };

    public final static PropertyReader<AbstractButton> SELECTED = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setSelected(value.asBoolean());
        }
    };

    public final static PropertyReader<AbstractButton> ICON_TEXT_GAP = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setIconTextGap(value.asInteger());
        }
    };

    public final static PropertyReader<AbstractButton> BORDER_PAINTED = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setBorderPainted(value.asBoolean());
        }
    };

    public final static PropertyReader<AbstractButton> FOCUS_PAINTED = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setFocusPainted(value.asBoolean());
        }
    };

    public final static PropertyReader<AbstractButton> ROLLOVER_ENABLED = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setRolloverEnabled(value.asBoolean());
        }
    };

    public final static PropertyReader<AbstractButton> CONTENT_AREA_FILLED = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setContentAreaFilled(value.asBoolean());
        }
    };

    public final static PropertyReader<AbstractButton> ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setIcon(value.asIcon());
        }
    };

    public final static PropertyReader<AbstractButton> DISABLED_ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setDisabledIcon(value.asIcon());
        }
    };

    public final static PropertyReader<AbstractButton> SELECTED_ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setSelectedIcon(value.asIcon());
        }
    };

    public final static PropertyReader<AbstractButton> PRESSED_ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setPressedIcon(value.asIcon());
        }
    };

    public final static PropertyReader<AbstractButton> ROLLOVER_ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setRolloverIcon(value.asIcon());
        }
    };

    public final static PropertyReader<AbstractButton> DISABLED_SELECTED_ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setDisabledSelectedIcon(value.asIcon());
        }
    };

    public final static PropertyReader<AbstractButton> ROLLOVER_SELECTED_ICON = new PropertyReader<AbstractButton>() {
        @Override
        public void read(AbstractButton component, Value value) {
            component.setRolloverSelectedIcon(value.asIcon());
        }
    };
}
