package org.develnext.jphp.swing.classes.events;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.AttributeSet;
import java.util.Collections;
import java.util.List;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(SwingExtension.NAMESPACE + "event\\HyperlinkEvent")
public class WrapHyperlinkEvent extends WrapSimpleEvent {
    protected HyperlinkEvent event;

    public WrapHyperlinkEvent(Environment env, HyperlinkEvent event) {
        super(env, event);
        this.event = event;
    }

    public WrapHyperlinkEvent(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected Memory __getUrl(Environment env, Memory... args) {
        return new StringMemory(event.getURL().toString());
    }

    @Signature
    protected Memory __getDescription(Environment env, Memory... args) {
        return new StringMemory(event.getDescription());
    }

    @Signature
    protected Memory __getAttributes(Environment env, Memory... args) {
        AttributeSet attrs = event.getSourceElement().getAttributes();
        List<?> keys = Collections.list(attrs.getAttributeNames());

        ArrayMemory result = new ArrayMemory();
        for(Object key : keys) {
            result.put(key.toString(), new StringMemory(attrs.getAttribute(key).toString()));
        }
        return result.toConstant();
    }
}
