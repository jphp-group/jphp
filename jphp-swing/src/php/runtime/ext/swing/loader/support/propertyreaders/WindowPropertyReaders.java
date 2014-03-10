package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;
import php.runtime.ext.swing.support.RootWindow;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WindowPropertyReaders extends PropertyReaders<Window> {

    protected final Map<String, PropertyReader<Window>> register = new HashMap<String, PropertyReader<Window>>(){{
        put("always-on-top", ALWAYS_ON_TOP);
        put("undecorated", UNDECORATED);
        put("resizable", RESIZABLE);
        put("title", TITLE);
        put("default-close-operation", DEFAULT_CLOSE_OPERATION);
        put("icon", ICON);
        put("type", TYPE);
    }};

    @Override
    protected Map<String, PropertyReader<Window>> getRegister() {
        return register;
    }

    @Override
    public Class<Window> getRegisterClass() {
        return Window.class;
    }

    public final static PropertyReader<Window> ALWAYS_ON_TOP = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            component.setAlwaysOnTop(value.asBoolean());
        }
    };

    public final static PropertyReader<Window> UNDECORATED = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            if (component instanceof RootWindow)
                ((RootWindow) component).setUndecorated(value.asBoolean());
        }
    };

    public final static PropertyReader<Window> RESIZABLE = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            if (component instanceof RootWindow)
                ((RootWindow) component).setResizable(value.asBoolean());
        }
    };

    public final static PropertyReader<Window> TITLE = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            if (component instanceof RootWindow)
                ((RootWindow) component).setTitle(value.asString());
        }

        @Override
        public boolean isTranslatable() {
            return true;
        }
    };

    public final static PropertyReader<Window> DEFAULT_CLOSE_OPERATION = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            if (component instanceof RootWindow)
                ((RootWindow) component).setDefaultCloseOperation(value.asInteger());
        }
    };

    public final static PropertyReader<Window> ICON = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            component.setIconImage(value.asIcon().getImage());
        }
    };

    public final static PropertyReader<Window> TYPE = new PropertyReader<Window>() {
        @Override
        public void read(Window component, Value value) {
            component.setType(Window.Type.valueOf(value.asString().toUpperCase()));
        }
    };
}
