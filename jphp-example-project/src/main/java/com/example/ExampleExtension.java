package com.example;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.zend.ext.ZendExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

public class ExampleExtension extends Extension {
    @Override
    public String getName() {
        return "Example";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String[] getRequiredExtensions() {
        return new String[] {
                SwingExtension.class.getName(),
                ZendExtension.class.getName()
        };
    }

    @Override
    public void onRegister(CompileScope scope) {
        // ...
        registerNativeClass(scope, Foobar.class);
    }

    public static class Foobar extends BaseObject {

        public Foobar(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Reflection.Signature
        public void hello(String arg) {
            System.out.println("Hello " + arg);
        }
    }
}
