package ru.regenix.jphp.cli;

import com.beust.jcommander.JCommander;
import php.runtime.Information;
import ru.regenix.jphp.compiler.AbstractCompiler;
import php.runtime.env.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.*;
import php.runtime.reflection.ModuleEntity;

import java.io.File;
import java.io.PrintStream;

public class CLI {

    private final CompileScope compileScope = new CompileScope();
    private final Arguments arguments;
    private final PrintStream output;
    private final JCommander commander;

    {
        compileScope.registerExtension(new CoreExtension());
        compileScope.registerExtension(new BCMathExtension());
        compileScope.registerExtension(new CTypeExtension());
        compileScope.registerExtension(new DateExtension());
        compileScope.registerExtension(new SPLExtension());
    }

    public CLI(JCommander commander, Arguments arguments, PrintStream output){
        this.commander = commander;
        this.output = output;
        this.arguments = arguments;
    }

    public void echo(String str){
        output.write(str.getBytes(), 0, str.length());
    }

    protected void showHelp(){
        StringBuilder builder = new StringBuilder();
        commander.usage(builder);
        echo(builder.toString());
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
        try {
            Context context = environment.createContext(file);

            AbstractCompiler compiler = new JvmCompiler(environment, context);
            ModuleEntity module = compiler.compile();
            compileScope.loadModule(module);
            environment.registerModule(module);

            module.includeNoThrow(environment);
        } catch (Exception e){
            environment.catchUncaught(e);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            try {
                environment.doFinal();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
    }

    public void process(){
        long t = System.currentTimeMillis();
        if (arguments.showHelp)
            showHelp();
        else if (arguments.showVersion){
            showVersion();
        } else if (arguments.file != null){
            executeFile(arguments.file);
        } else
            showHelp();

        if (arguments.showStat){
            output.println();
            output.println("---");
            output.printf("Time: %s ms", System.currentTimeMillis() - t);
        }
    }

    public static void main(String[] args){
        Arguments arguments = new Arguments();
        JCommander commander = new JCommander(arguments, args);
        CLI cli = new CLI(commander, arguments, System.out);
        cli.process();
    }
}
