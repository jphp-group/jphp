package org.develnext.jphp.swing.loader.support.propertyreaders;

import org.develnext.jphp.swing.loader.support.PropertyReader;
import org.develnext.jphp.swing.loader.support.Value;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JTabbedPanePropertyReaders extends PropertyReaders<JTabbedPane> {
    protected final Map<String, PropertyReader<JTabbedPane>> register = new HashMap<String, PropertyReader<JTabbedPane>>(){{
        put("tabs", TABS);
        put("selected-index", SELECTED_INDEX);
        put("tab-placement", TAB_PLACEMENT);
    }};

    @Override
    protected Map<String, PropertyReader<JTabbedPane>> getRegister() {
        return register;
    }

    @Override
    public Class<JTabbedPane> getRegisterClass() {
        return JTabbedPane.class;
    }

    public final static PropertyReader<JTabbedPane> TABS = new PropertyReader<JTabbedPane>() {
        @Override
        public void read(JTabbedPane component, Value value) {
            int i = 0;
            for(String tab : value.asArray(false)){
                component.setTitleAt(i, tab);
                i++;
            }
        }

        @Override
        public boolean isPostRead() {
            return true;
        }
    };

    public final static PropertyReader<JTabbedPane> SELECTED_INDEX = new PropertyReader<JTabbedPane>() {
        @Override
        public void read(JTabbedPane component, Value value) {
            component.setSelectedIndex(value.asInteger());
        }

        @Override
        public boolean isPostRead() {
            return true;
        }
    };

    public final static PropertyReader<JTabbedPane> TAB_PLACEMENT = new PropertyReader<JTabbedPane>() {
        @Override
        public void read(JTabbedPane component, Value value) {
            try {
                component.setTabPlacement(SwingConstants.class.getField(value.asString().toUpperCase()).getInt(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    };
}
