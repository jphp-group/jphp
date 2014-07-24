package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JLabelPropertyReaders extends PropertyReaders<JLabel> {

    protected final Map<String, PropertyReader<JLabel>> register = new HashMap<String, PropertyReader<JLabel>>(){{
        put("text", TEXT);
        put("icon-text-gap", ICON_TEXT_GAP);
        put("hor-align", HOR_ALIGN);
        put("ver-align", VER_ALIGN);
        put("icon", ICON);
        put("disabled-icon", DISABLED_ICON);
        put("hor-offset", HOR_TEXT_POSITION);
        put("ver-offset", VER_TEXT_POSITION);
    }};

    @Override
    protected Map<String, PropertyReader<JLabel>> getRegister() {
        return register;
    }

    @Override
    public Class<JLabel> getRegisterClass() {
        return JLabel.class;
    }

    public final static PropertyReader<JLabel> TEXT = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setText(value.asString());
        }

        @Override
        public boolean isTranslatable() {
            return true;
        }
    };

    public final static PropertyReader<JLabel> ICON_TEXT_GAP = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setIconTextGap(value.asInteger());
        }
    };

    public final static PropertyReader<JLabel> HOR_ALIGN = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setHorizontalAlignment(SwingExtension.toDirection(value.asMemory()));
        }
    };

    public final static PropertyReader<JLabel> VER_ALIGN = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setVerticalAlignment(SwingExtension.toDirection(value.asMemory()));
        }
    };

    public final static PropertyReader<JLabel> ICON = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setIcon(value.asIcon());
        }
    };

    public final static PropertyReader<JLabel> DISABLED_ICON = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setDisabledIcon(value.asIcon());
        }
    };

    public final static PropertyReader<JLabel> HOR_TEXT_POSITION = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setHorizontalTextPosition(SwingExtension.toDirection(value.asMemory()));
        }
    };

    public final static PropertyReader<JLabel> VER_TEXT_POSITION = new PropertyReader<JLabel>() {
        @Override
        public void read(JLabel component, Value value) {
            component.setVerticalTextPosition(SwingExtension.toDirection(value.asMemory()));
        }
    };
}
