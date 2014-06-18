package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class JFramePropertyReaders extends PropertyReaders<JFrame> {
    protected final Map<String, PropertyReader<JFrame>> register = new HashMap<String, PropertyReader<JFrame>>(){{
        put("maximized", MAXIMIZED);
    }};

    @Override
    protected Map<String, PropertyReader<JFrame>> getRegister() {
        return register;
    }

    @Override
    public Class<JFrame> getRegisterClass() {
        return JFrame.class;
    }

    public final static PropertyReader<JFrame> MAXIMIZED = new PropertyReader<JFrame>() {
        @Override
        public void read(JFrame component, Value value) {
            if (value.asBoolean()) {
                component.setExtendedState(component.getExtendedState() | Frame.MAXIMIZED_BOTH);
            } else {
                component.setExtendedState(Frame.NORMAL);
            }
        }
    };
}
