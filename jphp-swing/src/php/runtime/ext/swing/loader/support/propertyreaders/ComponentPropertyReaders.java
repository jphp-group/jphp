package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.ComponentProperties;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;
import php.runtime.ext.swing.misc.Align;
import php.runtime.ext.swing.misc.Anchor;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

final public class ComponentPropertyReaders extends PropertyReaders<Component> {

    protected final Map<String, PropertyReader<Component>> register = new HashMap<String, PropertyReader<Component>>(){{
        put("visible", VISIBLE);
        put("enabled", ENABLED);
        put("focusable", FOCUSABLE);
        put("x", X);
        put("y", Y);
        put("w", W);
        put("h", H);
        put("align", ALIGN);
        put("anchors", ANCHORS);
        put("group", GROUP);
        put("font", FONT);
        put("background", BACKGROUND);
        put("foreground", FOREGROUND);
        put("size", SIZE);
        put("position", POSITION);
        put("min-size", MIN_SIZE);
        put("autosize", AUTOSIZE);
    }};

    @Override
    protected Map<String, PropertyReader<Component>> getRegister() {
        return register;
    }

    @Override
    public Class<Component> getRegisterClass() {
        return Component.class;
    }

    public final static PropertyReader<Component> VISIBLE = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setVisible(value.asBoolean());
        }
    };

    public final static PropertyReader<Component> ENABLED = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setEnabled(value.asBoolean());
        }
    };

    public final static PropertyReader<Component> FOCUSABLE = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setFocusable(value.asBoolean());
        }
    };

    public final static PropertyReader<Component> X = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setLocation(value.asInteger(), component.getY());
        }
    };

    public final static PropertyReader<Component> Y = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setLocation(component.getX(), value.asInteger());
        }
    };

    public final static PropertyReader<Component> W = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setSize(value.asInteger(), component.getHeight());
        }
    };

    public final static PropertyReader<Component> H = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setSize(component.getWidth(), value.asInteger());
        }
    };

    public final static PropertyReader<Component> BACKGROUND = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setBackground(value.asColor());
        }
    };

    public final static PropertyReader<Component> FOREGROUND = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setForeground(value.asColor());
        }
    };

    public final static PropertyReader<Component> FONT = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setFont(value.asFont());
        }
    };

    public final static PropertyReader<Component> MIN_SIZE = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setMinimumSize(value.asDimension());
        }
    };

    public final static PropertyReader<Component> SIZE = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            if (value.asString().equals("auto")) {
                ComponentProperties properties = SwingExtension.getProperties(component);
                properties.setAutoSize(true);
            } else
                component.setSize(value.asDimension());
        }
    };

    public final static PropertyReader<Component> POSITION = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            component.setLocation(value.asPoint());
        }
    };

    public final static PropertyReader<Component> AUTOSIZE = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            ComponentProperties properties = SwingExtension.getProperties(component);
            properties.setAutoSize(value.asBoolean());
        }
    };

    public final static PropertyReader<Component> ALIGN = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            ComponentProperties properties = SwingExtension.getProperties(component);
            properties.setAlign(Align.valueOf(value.asString().toUpperCase()));
        }
    };

    public final static PropertyReader<Component> ANCHORS = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            ComponentProperties propertiies = SwingExtension.getProperties(component);
            propertiies.anchors.clear();
            for(String e : value.asArray()){
                Anchor anchor = Anchor.valueOf(e.toUpperCase());
                if (anchor != null)
                    propertiies.anchors.add(anchor);
            }
        }
    };

    public final static PropertyReader<Component> GROUP = new PropertyReader<Component>() {
        @Override
        public void read(Component component, Value value) {
            ComponentProperties properties = SwingExtension.getProperties(component);
            properties.setGroups(value.asString());
        }
    };

}
