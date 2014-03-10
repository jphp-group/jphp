package php.runtime.ext.swing.event;

import php.runtime.env.Environment;
import php.runtime.ext.swing.ComponentProperties;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class JTextComponentEventProvider extends EventProvider<JTextComponent> {
    @Override
    public Class<JTextComponent> getComponentClass() {
        return JTextComponent.class;
    }

    @Override
    public void register(final Environment env, JTextComponent component, final ComponentProperties properties) {
        component.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                triggerCaret(env, properties, "caretupdate", e);
            }
        });
    }

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return "caretupdate".equalsIgnoreCase(code);
    }
}
