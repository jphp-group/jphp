package org.develnext.jphp.ext.gui.desktop.classes;

import org.develnext.jphp.ext.gui.desktop.support.DesktopApi;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

@Reflection.Name(JavaFXExtension.NS + "UXDesktop")
public class UXDesktop extends BaseWrapper<Desktop> {
    public UXDesktop(Environment env, Desktop wrappedObject) {
        super(env, wrappedObject);
    }

    public UXDesktop(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = Desktop.getDesktop();
    }

    @Signature
    public boolean open(File file) throws IOException {
        /*if (getWrappedObject().isSupported(Desktop.Action.OPEN)) {
            getWrappedObject().open(file);
            return true;
        } else {*/
            return DesktopApi.open(file);
        //}
    }

    @Signature
    public boolean browse(URI uri) throws IOException {
        if (getWrappedObject().isSupported(Desktop.Action.BROWSE)) {
            try {
                getWrappedObject().browse(uri);
                return true;
            } catch (IOException e) {
                return DesktopApi.browse(uri);
            }
        } else {
            return DesktopApi.browse(uri);
        }
    }

    @Signature
    public boolean edit(File file) throws IOException {
        if (getWrappedObject().isSupported(Desktop.Action.EDIT)) {
            try {
                getWrappedObject().edit(file);
            } catch (IOException e) {
                return false;
            }

            return true;
        } else {
            return DesktopApi.edit(file);
        }
    }

    @Signature
    public double[] getCursorPosition() {
        Point location = MouseInfo.getPointerInfo().getLocation();

        return new double[] { location.getX(), location.getY() };
    }
}
