package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;
import php.runtime.ext.swing.support.JScrollPanel;

import java.util.HashMap;
import java.util.Map;

public class JScrollPanelPropertyReaders extends PropertyReaders<JScrollPanel> {
    protected final Map<String, PropertyReader<JScrollPanel>> register = new HashMap<String, PropertyReader<JScrollPanel>>(){{
        put("hor-scroll", HOR_SCROLL_POLICY);
        put("ver-scroll", VER_SCROLL_POLICY);
    }};

    @Override
    protected Map<String, PropertyReader<JScrollPanel>> getRegister() {
        return register;
    }

    @Override
    public Class<JScrollPanel> getRegisterClass() {
        return JScrollPanel.class;
    }

    public final static PropertyReader<JScrollPanel> HOR_SCROLL_POLICY = new PropertyReader<JScrollPanel>() {
        @Override
        public void read(JScrollPanel component, Value value) {
            component.setHorScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(value.asString().toUpperCase()));
        }
    };

    public final static PropertyReader<JScrollPanel> VER_SCROLL_POLICY = new PropertyReader<JScrollPanel>() {
        @Override
        public void read(JScrollPanel component, Value value) {
            component.setVerScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(value.asString().toUpperCase()));
        }
    };
}
