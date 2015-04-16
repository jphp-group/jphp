package org.develnext.jphp.ext.libgdx.classes.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import org.develnext.jphp.ext.libgdx.classes.files.WrapFileHandle;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name(LibGDXExtension.NAMESPACE + "assets\\AssetManager")
@WrapInterface(value = WrapAssetManager.Methods.class)
public class WrapAssetManager extends BaseWrapper<AssetManager> {
    public WrapAssetManager(Environment env, AssetManager wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapAssetManager(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new AssetManager();
    }

    @Signature
    public void __construct(final Invoker resolver) {
        __wrappedObject = new AssetManager(new FileHandleResolver() {
            @Override
            public FileHandle resolve(String fileName) {
                Memory r = resolver.callAny(fileName);
                if (r.instanceOf(WrapFileHandle.class)) {
                    return r.toObject(WrapFileHandle.class).getWrappedObject();
                }

                return null;
            }
        });
    }

    @Signature
    public Memory get(Environment env, TraceInfo trace, String fileName) {
        Object object = __wrappedObject.get(fileName);
        if (object == null) {
            return null;
        }

        MemoryOperation operation = MemoryOperation.get(object.getClass(), null);
        if (operation == null) {
            return null;
        }

        return operation.unconvertNoThow(env, trace, object);
    }

    @Signature
    public void loadTexture(String fileName) {
        __wrappedObject.load(fileName, Texture.class);
    }

    @Signature
    public void loadMusic(String fileName) {
        __wrappedObject.load(fileName, Music.class);
    }

    @Signature
    public void loadSound(String fileName) {
        __wrappedObject.load(fileName, Sound.class);
    }

    @Signature
    public void loadPixmap(String fileName) {
        __wrappedObject.load(fileName, Pixmap.class);
    }

    @Signature
    public boolean containsAsset(Memory memory) {
        if (memory.isObject() && memory.toValue(ObjectMemory.class).value instanceof BaseWrapper) {
            return __wrappedObject.containsAsset(memory.toObject(BaseWrapper.class).getWrappedObject());
        }

        return false;
    }

    @Signature
    public String getAssetFileName(Memory memory) {
        if (memory.isObject() && memory.toValue(ObjectMemory.class).value instanceof BaseWrapper) {
            return __wrappedObject.getAssetFileName(memory.toObject(BaseWrapper.class).getWrappedObject());
        }

        return null;
    }

    interface Methods {
        void unload (String fileName);
        boolean isLoaded (String fileName);
        void disposeDependencies (String fileName);
        boolean update ();
        boolean update (int millis);
        void finishLoading ();
        int getLoadedAssets ();
        int getQueuedAssets ();
        float getProgress ();
        void dispose ();
        void clear ();
        int getReferenceCount (String fileName);
        void setReferenceCount (String fileName, int refCount);
        String getDiagnostics ();
    }
}
