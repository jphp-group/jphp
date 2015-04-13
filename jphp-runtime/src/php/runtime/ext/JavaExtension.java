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
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new JavaFunctions());

        registerClass(scope, JavaException.class);
        registerClass(scope, JavaReflection.class);
        registerClass(scope, JavaObject.class);
        registerClass(scope, JavaClass.class);
        registerClass(scope, JavaMethod.class);
        registerClass(scope, JavaField.class);
    }
}
