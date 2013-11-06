package ru.regenix.jphp.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.env.Environment;

public class OutputFunctions extends FunctionsContainer {

    public static void echo(Environment environment, Memory memory){
        environment.echo(memory.toString());
    }
}
