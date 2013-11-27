package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.ext.core.*;
import ru.regenix.jphp.runtime.ext.core.classes.ArrayAccess;
import ru.regenix.jphp.runtime.ext.core.classes.StdClass;

public class CoreExtension extends Extension {
    @Override
    public String getName() {
        return "Core";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new InfoFunctions());

        registerConstants(new MathConstants());
        registerFunctions(new MathFunctions());
        registerFunctions(new StringFunctions());
        registerFunctions(new OutputFunctions());

        registerNativeClass(StdClass.class);
        registerNativeClass(ArrayAccess.class);
    }
}
