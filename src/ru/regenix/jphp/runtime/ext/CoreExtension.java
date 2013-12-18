package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.ext.core.*;
import ru.regenix.jphp.runtime.lang.spl.ArrayAccess;
import ru.regenix.jphp.runtime.lang.StdClass;

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
        registerFunctions(new LangFunctions());
        registerFunctions(new InfoFunctions());

        registerConstants(new MathConstants());
        registerFunctions(new MathFunctions());
        registerConstants(new StringConstants());
        registerFunctions(new StringFunctions());
        registerConstants(new ArrayConstants());
        registerFunctions(new ArrayFunctions());
        registerFunctions(new OutputFunctions());

        registerNativeClass(StdClass.class);
        registerNativeClass(ArrayAccess.class);
    }
}
