package ru.regenix.jphp.runtime.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.env.Environment;

public class OutputFunctions extends FunctionsContainer {

    public static Memory print(Environment environment, Memory memory){
        environment.echo(memory.toString());
        return Memory.CONST_INT_1;
    }
}
