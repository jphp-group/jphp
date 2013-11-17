package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.ext.core.MathConstants;
import ru.regenix.jphp.runtime.ext.core.MathFunctions;
import ru.regenix.jphp.runtime.ext.core.OutputFunctions;
import ru.regenix.jphp.runtime.ext.core.classes.ArrayAccess;

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
        registerConstants(new MathConstants());
        registerFunctions(new MathFunctions());
        registerFunctions(new OutputFunctions());

        registerNativeClass(ArrayAccess.class);
    }
}
