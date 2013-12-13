package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.support.Memory;

public class LangFunctions extends FunctionsContainer {

    public static boolean isset(@Runtime.Reference Memory memory){
        return !memory.isNull();
    }

    public static boolean empty(@Runtime.Reference Memory memory){
        return !memory.toBoolean();
    }
}
