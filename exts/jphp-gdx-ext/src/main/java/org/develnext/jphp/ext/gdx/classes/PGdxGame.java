package org.develnext.jphp.ext.gdx.classes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import org.develnext.jphp.ext.gdx.GdxExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Name("GdxGame")
@Namespace(GdxExtension.NS)
public class PGdxGame extends BaseObject {
    public enum HandlerType {
        create, resize, render, pause, resume, dispose
    }

    private final Map<HandlerType, Invoker> handlers = new LinkedHashMap<>();

    public PGdxGame(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public PGdxGame on(HandlerType type, @Nullable Invoker invoker) {
        handlers.put(type, invoker);
        return this;
    }

    @Signature
    public PGdxGame off(HandlerType type) {
        handlers.remove(type);
        return this;
    }

    @Signature
    @SuppressWarnings("unchecked")
    public PGdxGame trigger(HandlerType type, Memory... args) {
        Invoker invoker = handlers.get(type);
        if (invoker != null) {
            invoker.callAny(args);
        }

        return this;
    }

    @Signature
    public PGdxGame later(Invoker invoker) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                invoker.callAny();
            }
        });

        return this;
    }

    @Signature
    public PGdxGame exit() {
        Gdx.app.exit();
        return this;
    }

    public ApplicationListener createListener() {
        return new ApplicationListener() {
            @Override
            public void create() {
                trigger(HandlerType.create);
            }

            @Override
            public void resize(int width, int height) {
                trigger(HandlerType.resize, LongMemory.valueOf(width), LongMemory.valueOf(height));
            }

            @Override
            public void render() {
                trigger(HandlerType.render);
            }

            @Override
            public void pause() {
                trigger(HandlerType.pause);
            }

            @Override
            public void resume() {
                trigger(HandlerType.resume);
            }

            @Override
            public void dispose() {
                trigger(HandlerType.dispose);
            }
        };
    }
}
