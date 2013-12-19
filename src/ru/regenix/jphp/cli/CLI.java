package ru.regenix.jphp.cli;

import com.beust.jcommander.JCommander;
import ru.regenix.jphp.Information;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.DieException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.ext.BCMathExtension;
import ru.regenix.jphp.runtime.ext.CTypeExtension;
import ru.regenix.jphp.runtime.ext.CoreExtension;
import ru.regenix.jphp.runtime.ext.DateExtension;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.runtime.util.JVMStackTracer;

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
        Context context = environment.createContext(file);

        AbstractCompiler compiler = new JvmCompiler(environment, context);
        ModuleEntity module = compiler.compile();
        compileScope.loadModule(module);
        environment.registerModule(module);

        module.includeNoThrow(environment);
    }

    public void process(){
        long t = System.currentTimeMillis();
        try {
            if (arguments.showHelp)
                showHelp();
            else if (arguments.showVersion){
                showVersion();
            } else if (arguments.file != null){
                executeFile(arguments.file);
            } else
                showHelp();
        } catch (ErrorException e){
            output.printf("[%s] %s", e.getType().getTypeName(), e.getMessage());
            output.println();
            output.printf("    at line %s", (e.getTraceInfo().getStartLine() + 1));
            output.printf(", position %s", (e.getTraceInfo().getStartPosition() + 1));
            output.println();
            output.println("    in '" + e.getTraceInfo().getFileName() + "'");

            System.out.println();
            JVMStackTracer tracer = compileScope.getStackTracer(e);
            for(JVMStackTracer.Item el : tracer){
                System.out.println("\tat " + (el.isInternal() ? "" : "-> ") + el);
            }

        } catch (DieException e){
            System.exit(e.getExitCode());
        }
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
