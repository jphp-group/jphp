package php.runtime.ext.swing.event;

import php.runtime.env.Environment;
import php.runtime.ext.swing.ComponentProperties;
import php.runtime.ext.swing.support.JTextAreaX;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;

public class JTextAreaEventProvider extends EventProvider<JTextAreaX> {
    @Override
    public Class<JTextAreaX> getComponentClass() {
        return JTextAreaX.class;
    }

    @Override
    public void register(final Environment env, JTextAreaX component, final ComponentProperties properties) {
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
