package com.example;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.zend.ext.ZendExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static php.runtime.annotation.Reflection.Nullable;
import static php.runtime.annotation.Reflection.Signature;

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
        registerClass(scope, Foobar.class);
    }

    public static class Foobar extends BaseObject {
        public Foobar(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        public void __construct(Iterable<List<Date>> value) {
            for (List<Date> dates : value) {
                for (Date el : dates) {
                    System.out.println(el);
                }
                System.out.println("-----");
            }
        }
    }
}
