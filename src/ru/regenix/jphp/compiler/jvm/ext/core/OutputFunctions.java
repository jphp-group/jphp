package ru.regenix.jphp.compiler.jvm.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.env.Environment;

public class OutputFunctions extends FunctionsContainer {

    public static void echo(Environment environment, Memory memory){
        environment.echo(memory.toString());
    }

    public static void header(Environment environment, Memory memory){
        environment.header(memory.toString());
    }
}
