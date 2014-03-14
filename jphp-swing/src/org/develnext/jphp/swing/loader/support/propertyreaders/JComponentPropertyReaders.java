package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.BorderUtils;
import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

final public class JComponentPropertyReaders extends PropertyReaders<JComponent> {

    protected final Map<String, PropertyReader<JComponent>> register = new HashMap<String, PropertyReader<JComponent>>(){{
        put("tooltip-text", TOOLTIP_TEXT);
        put("autoscrolls", AUTOSCROLLS);
        put("border", BORDER);
        put("opaque", OPAQUE);
        put("double-buffered", DOUBLE_BUFFERED);
    }};

    @Override
    protected Map<String, PropertyReader<JComponent>> getRegister() {
        return register;
    }

    @Override
    public Class<JComponent> getRegisterClass() {
        return JComponent.class;
    }

    public final static PropertyReader<JComponent> TOOLTIP_TEXT = new PropertyReader<JComponent>() {
        @Override
        public void read(JComponent component, Value value) {
            component.setToolTipText(value.asString());
        }

        @Override
        public boolean isTranslatable() {
            return true;
        }
    };

    public final static PropertyReader<JComponent> AUTOSCROLLS = new PropertyReader<JComponent>() {
        @Override
        public void read(JComponent component, Value value) {
            component.setAutoscrolls(value.asBoolean());
        }
    };

    public final static PropertyReader<JComponent> OPAQUE = new PropertyReader<JComponent>() {
        @Override
        public void read(JComponent component, Value value) {
            component.setOpaque(value.asBoolean());
        }
    };

    public final static PropertyReader<JComponent> DOUBLE_BUFFERED = new PropertyReader<JComponent>() {
        @Override
        public void read(JComponent component, Value value) {
            component.setDoubleBuffered(value.asBoolean());
        }
    };

    public final static PropertyReader<JComponent> BORDER = new PropertyReader<JComponent>() {
        @Override
        public void read(JComponent component, Value value) {
            try {
                component.setBorder(BorderUtils.decode(value.asString()));
            } catch (NumberFormatException e) {
                component.setBorder(BorderFactory.createEmptyBorder());
            }
        }
    };

}
