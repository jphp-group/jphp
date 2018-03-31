package org.develnext.jphp.ext.gdx;

import org.develnext.jphp.ext.gdx.classes.PGdxGame;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class GdxExtension extends Extension {
    public static final String NS = "gdx";

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
        registerClass(scope, PGdxGame.class);
    }
}
