package org.develnext.jphp.ext.gui.desktop.classes;


import org.develnext.jphp.ext.gui.desktop.GuiDesktopExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

@Namespace(GuiDesktopExtension.NS)
public class Runtime extends BaseObject {
    public Runtime(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static int availableProcessors() {
        return java.lang.Runtime.getRuntime().availableProcessors();
    }

    @Signature
    public static long freeMemory() {
        return java.lang.Runtime.getRuntime().freeMemory();
    }

    @Signature
    public static long maxMemory() {
        return java.lang.Runtime.getRuntime().maxMemory();
    }

    @Signature
    public static long totalMemory() {
        return java.lang.Runtime.getRuntime().totalMemory();
    }

    @Signature
    public static void addJar(Environment env, File file) throws IOException {
        try {
            env.getScope().getClassLoader().addLibrary(file.toURI().toURL());
        } catch (Throwable t) {
            throw new IOException("Error, could not add URL to system classloader, " + t.getMessage());
        }//end try catch
    }
}
