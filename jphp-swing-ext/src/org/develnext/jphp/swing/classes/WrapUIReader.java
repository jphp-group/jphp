package org.develnext.jphp.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.stream.Stream;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.RootObject;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import org.develnext.jphp.swing.loader.UIReader;
import org.develnext.jphp.swing.loader.support.Value;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.io.InputStream;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIReader")
public class WrapUIReader extends RootObject {
    protected UIReader reader;

    public WrapUIReader(Environment env, UIReader reader) {
        super(env);
        this.reader = reader;
    }

    public WrapUIReader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory __construct(Environment env, Memory... args) {
        reader = new UIReader();
        return Memory.NULL;
    }

    @Signature(@Arg(value = "handler", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public Memory onRead(final Environment env, Memory... args) {
        if (args[0].isNull())
            reader.setReadHandler(null);
        else {
            final Invoker invoker = Invoker.valueOf(env, null, args[0]);
            UIReader.ReadHandler handler = new UIReader.ReadHandler() {
                @Override
                public void onRead(Component component, String var) {
                    invoker.callNoThrow(
                            new ObjectMemory(UIElement.of(env, component)),
                            var == null ? Memory.NULL : new StringMemory(var)
                    );
                }
            };

            reader.setReadHandler(handler);
        }
        return Memory.NULL;
    }

    @Signature(@Arg(value = "handler", type = HintType.CALLABLE, optional = @Optional("NULL")))
    public Memory onTranslate(final Environment env, Memory... args) {
        if (args[0].isNull())
            reader.setTranslateHandler(null);
        else {
            final Invoker invoker = Invoker.valueOf(env, null, args[0]);
            UIReader.TranslateHandler handler = new UIReader.TranslateHandler() {
                @Override
                public Value onTranslate(Component component, Value var) {
                    return new Value(invoker.callNoThrow(
                            new ObjectMemory(UIElement.of(env, component)),
                            var == null ? Memory.NULL : new StringMemory(var.asString())
                    ).toString());
                }
            };

            reader.setTranslateHandler(handler);
        }
        return Memory.NULL;
    }

    @Signature({
            @Arg("stream")
    })
    public Memory read(final Environment env, Memory... args) {
        InputStream stream = Stream.getInputStream(env, args[0]);
        try {
            Component component = reader.read(stream);
            return component == null ? Memory.NULL
                    : new ObjectMemory(UIElement.of(env, component));
        } finally {
            Stream.closeStream(env, stream);
        }
    }
}
