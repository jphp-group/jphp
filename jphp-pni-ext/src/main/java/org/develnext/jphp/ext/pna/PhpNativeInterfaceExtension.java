package org.develnext.jphp.ext.pna;

import org.develnext.jphp.ext.pna.classes.NativeObject;
import org.develnext.jphp.ext.pna.support.NativeClassEntity;
import php.runtime.Information;
import php.runtime.env.CompileScope;
import php.runtime.env.handler.EntityFetchHandler;
import php.runtime.ext.support.Extension;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;
import php.runtime.wrap.ClassWrapper;

import java.util.HashMap;
import java.util.Map;

public class PhpNativeInterfaceExtension extends Extension {
    public static final String NS = "php\\native";

    protected Map<String, NativeClassEntity> classes = new HashMap<>();

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    public static String getNativeName(Class<?> type) {
        return "j\\" + type.getName().replace('.', Information.NAMESPACE_SEP_CHAR);
    }

    @Override
    public void onRegister(CompileScope scope) {
        //registerWrapperClass(scope, NativeObject.class, );
        scope.addClassEntityFetchHandler(new EntityFetchHandler() {
            @Override
            public void fetch(CompileScope scope, String name, String lowerName) {
            if (name.startsWith("j\\")) {
                String originName = name;

                name = name.substring(2).replace(Information.NAMESPACE_SEP_CHAR, '.');

                try {
                    Class<?> cls = Class.forName(name, true, scope.getClassLoader());
                    NativeClassEntity classEntity = new NativeClassEntity(scope, cls);

                    classEntity.setName(originName);

                    scope.registerClass(classEntity);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            }
        });
    }
}
