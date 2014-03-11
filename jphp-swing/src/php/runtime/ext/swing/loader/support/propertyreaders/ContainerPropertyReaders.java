package php.runtime.ext.swing.loader.support.propertyreaders;

import php.runtime.ext.swing.XYLayout;
import php.runtime.ext.swing.loader.support.PropertyReader;
import php.runtime.ext.swing.loader.support.Value;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ContainerPropertyReaders extends PropertyReaders<Container> {
    protected final Map<String, PropertyReader<Container>> register = new HashMap<String, PropertyReader<Container>>(){{
        put("layout", LAYOUT);
        put("inner-size", INNER_SIZE);
    }};

    @Override
    protected Map<String, PropertyReader<Container>> getRegister() {
        return register;
    }

    @Override
    public Class<Container> getRegisterClass() {
        return Container.class;
    }

    public final static PropertyReader<Container> INNER_SIZE = new PropertyReader<Container>() {
        @Override
        public void read(Container component, Value value) {
            Insets insets = component.getInsets();
            Dimension dimension = value.asDimension();

            component.setSize(new Dimension(
                    dimension.width - insets.left + insets.right,
                    dimension.height - insets.top + insets.bottom
            ));
        }
    };

    public final static PropertyReader<Container> LAYOUT = new PropertyReader<Container>() {
        @Override
        public void read(Container component, Value value) {
            String s = value.asString().toLowerCase();
            Value arg = null;
            if (s.indexOf('(') > -1 && s.lastIndexOf(')') > s.indexOf('(')) {
                arg = new Value(s.substring(s.indexOf('(') + 1, s.lastIndexOf(')')));
                s = s.substring(0, s.indexOf('('));
            }

            if ("absolute".equals(s))
                component.setLayout(new XYLayout());
            else if ("grid".equals(s)) {
                if (arg == null)
                    component.setLayout(new GridLayout());
                else
                    component.setLayout(new GridLayout(arg.asDimension().width, arg.asDimension().height));
            } else if ("flow".equals(s))
                component.setLayout(new FlowLayout());
            else if ("grid-bag".equals(s))
                component.setLayout(new GridBagLayout());
            else if ("border".equals(s))
                component.setLayout(new BorderLayout());
            else if ("card".equals(s))
                component.setLayout(new CardLayout());
        }
    };
}
