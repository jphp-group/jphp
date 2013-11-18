package ru.regenix.jphp.cli;

import com.beust.jcommander.JCommander;
import ru.regenix.jphp.Information;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;

import java.io.File;
import java.io.PrintStream;

public class CLI {

    private final CompileScope compileScope = new CompileScope();
    private final Arguments arguments;
    private final PrintStream output;

    public CLI(Arguments arguments, PrintStream output){
        this.output = output;
        this.arguments = arguments;
    }

    public void echo(String str){
        output.write(str.getBytes(), 0, str.length());
    }

    protected void showVersion(){
        output.printf("%s %s like PHP %s",
                Information.NAME, Information.CORE_VERSION, Information.LIKE_PHP_VERSION
        );
        output.println();
        output.println("Copyright (c) " + Information.COPYRIGHT);
        output.println("License: " + Information.LICENSE);
        output.println();
    }

    protected void executeFile(String filename){
        File file = new File(filename);
        Environment environment = new Environment(compileScope, output);
        Context context = environment.createContext(file);

        AbstractCompiler compiler = new JvmCompiler(environment, context);
        ModuleEntity module = compiler.compile();
        compileScope.loadModule(module);

        module.includeNoThrow(environment);
    }

    public void process(){
        try {
            if (arguments.showVersion){
                showVersion();
                return;
            }

            if (arguments.file != null){
                executeFile(arguments.file);
                return;
            }
        } catch (ErrorException e){
            output.printf("[%s] %s", e.getType().getTypeName(), e.getMessage());
        }
    }

    public static void main(String[] args){
        Arguments arguments = new Arguments();
        new JCommander(arguments, args);
        CLI cli = new CLI(arguments, System.out);
        cli.process();
    }
}
