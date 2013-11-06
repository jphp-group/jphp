package ru.regenix.jphp.compiler.jvm.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.ext.core.MathConstants;
import ru.regenix.jphp.compiler.jvm.ext.core.MathFunctions;
import ru.regenix.jphp.compiler.jvm.ext.core.OutputFunctions;

public class CoreExtension extends JvmExtension {
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
