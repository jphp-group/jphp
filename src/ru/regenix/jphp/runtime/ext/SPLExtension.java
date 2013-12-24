package ru.regenix.jphp.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.runtime.ext.spl.SPLFunctions;
import ru.regenix.jphp.runtime.lang.spl.Countable;
import ru.regenix.jphp.runtime.lang.spl.iterator.OuterIterator;
import ru.regenix.jphp.runtime.lang.spl.iterator.RecursiveIterator;
import ru.regenix.jphp.runtime.lang.spl.iterator.SeekableIterator;

public class SPLExtension extends Extension {
    @Override
    public String getName() {
        return "SPL";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new SPLFunctions());

        registerNativeClass(scope, Countable.class);
        registerNativeClass(scope, OuterIterator.class);
        registerNativeClass(scope, RecursiveIterator.class);
        registerNativeClass(scope, SeekableIterator.class);
    }
}
