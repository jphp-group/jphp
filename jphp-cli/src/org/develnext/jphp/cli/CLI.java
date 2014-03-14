package org.develnext.jphp.cli;

import com.beust.jcommander.JCommander;
import php.runtime.Information;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.launcher.Launcher;
import php.runtime.reflection.ModuleEntity;

import java.io.File;
import java.io.PrintStream;

public class CLI {
    private final Arguments arguments;
    private final PrintStream output;
    private final JCommander commander;
    private final String[] args;

    public CLI(JCommander commander, Arguments arguments, PrintStream output, String[] args){
        this.commander = commander;
        this.output = output;
        this.arguments = arguments;
        this.args = args;
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

    protected void executeFile(String filename) throws Throwable {
        Launcher launcher = new Launcher("jphp.conf", args);
        launcher.run(false);

        File file = new File(filename);
        Environment environment = new Environment(launcher.getCompileScope(), output);
        environment.getDefaultBuffer().setImplicitFlush(true);

        try {
            Context context = new Context(file);
            ModuleEntity module = environment.importModule(context);

            module.include(environment);
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

    public void process() throws Throwable {
        if (arguments.showHelp)
            showHelp();
        else if (arguments.showVersion){
            showVersion();
        } else if (arguments.file != null){
            executeFile(arguments.file);
        } else
            showHelp();
    }

    public static void main(String[] args) throws Throwable {
        Arguments arguments = new Arguments();
        JCommander commander = new JCommander(arguments, args);
        CLI cli = new CLI(commander, arguments, System.out, args);
        cli.process();
    }
}
