package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.ext.java.*;
import php.runtime.ext.support.Extension;

public class JavaExtension extends Extension {
    @Override
    public String getName() {
        return "Java";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new JavaFunctions());

        registerNativeClass(scope, JavaException.class);
        registerNativeClass(scope, JavaReflection.class);
        registerNativeClass(scope, JavaObject.class);
        registerNativeClass(scope, JavaClass.class);
        registerNativeClass(scope, JavaMethod.class);
        registerNativeClass(scope, JavaField.class);
    }
}
