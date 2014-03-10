package php.runtime.ext.swing.event;

import php.runtime.env.Environment;
import php.runtime.ext.swing.ComponentProperties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class JSliderEventProvider extends EventProvider<JSlider> {
    @Override
    public Class<JSlider> getComponentClass() {
        return JSlider.class;
    }

    @Override
    public void register(final Environment env, final JSlider component, final ComponentProperties properties) {
        component.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                triggerSimple(env, properties, "change", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return "change".equalsIgnoreCase(code);
    }
}
