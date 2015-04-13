package org.develnext.jphp.ext.libgdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.develnext.jphp.ext.libgdx.bind.ApplicationListenerMemoryOperation;
import org.develnext.jphp.ext.libgdx.bind.DisplayModeArrayMemoryOperation;
import org.develnext.jphp.ext.libgdx.classes.*;
import org.develnext.jphp.ext.libgdx.classes.assets.WrapAssetManager;
import org.develnext.jphp.ext.libgdx.classes.audio.WrapAudioDevice;
import org.develnext.jphp.ext.libgdx.classes.audio.WrapAudioRecorder;
import org.develnext.jphp.ext.libgdx.classes.audio.WrapMusic;
import org.develnext.jphp.ext.libgdx.classes.audio.WrapSound;
import org.develnext.jphp.ext.libgdx.classes.files.WrapFileHandle;
import org.develnext.jphp.ext.libgdx.classes.graphics.*;
import org.develnext.jphp.ext.libgdx.classes.math.WrapPolygon;
import org.develnext.jphp.ext.libgdx.classes.math.WrapPolyline;
import org.develnext.jphp.ext.libgdx.classes.math.WrapVector2;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class LibGDXExtension extends Extension {
    public final static String NAMESPACE = "php\\gdx\\";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerMemoryOperation(DisplayModeArrayMemoryOperation.class);

        registerJavaException(scope, WrapGdxRuntimeException.class, GdxRuntimeException.class);

        registerWrapperClass(scope, Vector2.class, WrapVector2.class);
        registerWrapperClass(scope, Polygon.class, WrapPolygon.class);
        registerWrapperClass(scope, Polyline.class, WrapPolyline.class);

        registerWrapperClass(scope, Graphics.DisplayMode.class, WrapDisplayMode.class);

        registerClass(scope, WrapApplicationListener.class);
        registerMemoryOperation(ApplicationListenerMemoryOperation.class);

        registerWrapperClass(scope, FileHandle.class, WrapFileHandle.class);
        registerWrapperClass(scope, Files.class, WrapFiles.class);

        registerWrapperClass(scope, Pixmap.class, WrapPixmap.class);
        registerWrapperClass(scope, Texture.class, WrapTexture.class);
        registerWrapperClass(scope, Batch.class, WrapBatch.class);
        registerWrapperClass(scope, SpriteBatch.class, WrapSpriteBatch.class);
        registerWrapperClass(scope, Sprite.class, WrapSprite.class);
        registerWrapperClass(scope, Graphics.class, WrapGraphics.class);

        registerWrapperClass(scope, LwjglApplication.class, WrapLwjglApplication.class);
        registerWrapperClass(scope, LwjglApplicationConfiguration.class, WrapLwjglApplicationConfiguration.class);

        registerWrapperClass(scope, Input.class, WrapInput.class);

        registerWrapperClass(scope, AudioDevice.class, WrapAudioDevice.class);
        registerWrapperClass(scope, AudioRecorder.class, WrapAudioRecorder.class);
        registerWrapperClass(scope, Sound.class, WrapSound.class);
        registerWrapperClass(scope, Music.class, WrapMusic.class);
        registerWrapperClass(scope, Audio.class, WrapAudio.class);

        registerWrapperClass(scope, AssetManager.class, WrapAssetManager.class);
        registerWrapperClass(scope, Application.class, WrapApplication.class);
        registerClass(scope, WrapGdx.class);
    }
}
