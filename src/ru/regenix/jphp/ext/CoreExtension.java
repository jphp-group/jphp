package ru.regenix.jphp.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.ext.core.MathConstants;
import ru.regenix.jphp.ext.core.MathFunctions;
import ru.regenix.jphp.ext.core.OutputFunctions;

public class CoreExtension extends Extension {
    @Override
    public String getName() {
        return "Core";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerConstants(new MathConstants());
        registerFunctions(new MathFunctions());
        registerFunctions(new OutputFunctions());
    }
}
