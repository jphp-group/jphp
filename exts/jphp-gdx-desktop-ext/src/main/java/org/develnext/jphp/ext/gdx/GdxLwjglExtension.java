package org.develnext.jphp.ext.gdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import org.develnext.jphp.ext.gdx.classes.PLwjglApplication;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class GdxLwjglExtension extends Extension {
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "gdx" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PLwjglApplication.class);
    }
}
