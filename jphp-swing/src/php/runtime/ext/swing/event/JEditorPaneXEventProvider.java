package php.runtime.ext.swing.event;

import php.runtime.env.Environment;
import php.runtime.ext.swing.ComponentProperties;
import php.runtime.ext.swing.support.JEditorPaneX;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class JEditorPaneXEventProvider extends EventProvider<JEditorPaneX> {

    @Override
    public Class<JEditorPaneX> getComponentClass() {
        return JEditorPaneX.class;
    }

    @Override
    public void register(final Environment env, JEditorPaneX component, final ComponentProperties properties) {
        component.getContent().addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    triggerSimple(env, properties, "linkclick", e);
                } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                    triggerSimple(env, properties, "linkenter", e);
                } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                    triggerSimple(env, properties, "linkexit", e);
                }
            }
        });
    }

    private final static Set<String> allowed = new HashSet<String>() {{
        add("linkclick");
        add("linkenter");
        add("linkexit");
    }};

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return allowed.contains(code.toLowerCase());
    }
}
