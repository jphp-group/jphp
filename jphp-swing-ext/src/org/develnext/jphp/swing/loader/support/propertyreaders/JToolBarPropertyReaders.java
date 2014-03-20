package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JToolBarPropertyReaders extends PropertyReaders<JToolBar> {
    protected final Map<String, PropertyReader<JToolBar>> register = new HashMap<String, PropertyReader<JToolBar>>(){{
        put("floatable", FLOATABLE);
        put("vertical", VERTICAL);
        put("rollover", ROLLOVER);
    }};

    @Override
    protected Map<String, PropertyReader<JToolBar>> getRegister() {
        return register;
    }

    @Override
    public Class<JToolBar> getRegisterClass() {
        return JToolBar.class;
    }

    public final static PropertyReader<JToolBar> FLOATABLE = new PropertyReader<JToolBar>() {
        @Override
        public void read(JToolBar component, Value value) {
            component.setFloatable(value.asBoolean());
        }
    };

    public final static PropertyReader<JToolBar> VERTICAL = new PropertyReader<JToolBar>() {
        @Override
        public void read(JToolBar component, Value value) {
            component.setOrientation(value.asBoolean() ? SwingConstants.VERTICAL : SwingConstants.HORIZONTAL);
        }
    };

    public final static PropertyReader<JToolBar> ROLLOVER = new PropertyReader<JToolBar>() {
        @Override
        public void read(JToolBar component, Value value) {
            component.setRollover(value.asBoolean());
        }
    };
}
