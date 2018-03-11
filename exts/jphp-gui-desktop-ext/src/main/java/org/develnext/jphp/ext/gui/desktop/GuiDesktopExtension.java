package org.develnext.jphp.ext.gui.desktop;

import org.develnext.jphp.ext.gui.desktop.classes.*;
import org.develnext.jphp.ext.gui.desktop.classes.Robot;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.env.handler.EntityFetchHandler;

import java.awt.*;

public class GuiDesktopExtension extends JavaFXExtension {
    public static final String NS = "php\\desktop";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "gui", "desktop" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, Mouse.class);
        registerClass(scope, MouseEx.class);
        registerClass(scope, org.develnext.jphp.ext.gui.desktop.classes.Runtime.class);

        registerWrapperClass(scope, java.awt.Robot.class, Robot.class);
        registerWrapperClass(scope, Desktop.class, UXDesktop.class);
    }

    @Override
    public void onLoad(Environment env) {
        super.onLoad(env);
    }
}
