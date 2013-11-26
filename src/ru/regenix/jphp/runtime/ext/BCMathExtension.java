package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.ext.bcmath.BCMathFunctions;

public class BCMathExtension extends Extension {
    @Override
    public String getName() {
        return "bcmath";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new BCMathFunctions());
    }
}
