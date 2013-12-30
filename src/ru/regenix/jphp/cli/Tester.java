package ru.regenix.jphp.cli;

import com.beust.jcommander.JCommander;
import org.apache.commons.io.FileUtils;
import ru.regenix.jphp.tester.Test;
import ru.regenix.jphp.compiler.CompileScope;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    private final CompileScope compileScope = new CompileScope();
    private final TestArguments arguments;
    private final PrintStream output;
    private final JCommander commander;

    private List<File> files;

    public Tester(JCommander commander, TestArguments arguments, PrintStream output){
        this.commander = commander;
        this.output = output;
        this.arguments = arguments;

        if (arguments.modified) {
            arguments.recursive = true;
            arguments.coverage = true;
        }

        files = new ArrayList<File>();
        for(String path : arguments.paths){
            File pathFile = new File(path);
            if (pathFile.isFile() && pathFile.getName().endsWith(".phpt")){
                files.add(pathFile);
            } else if (pathFile.isDirectory()) {
                files.addAll(FileUtils.listFiles(new File(path), new String[]{"phpt"}, arguments.recursive));
            } else {
                // TODO: error
            }
        }
    }

    public void process(){
        for(File file : files){
            Test test = new Test(file);
            test.run(output);
        }
    }

    public static void main(String[] args){
        TestArguments arguments = new TestArguments();
        JCommander commander = new JCommander(arguments, args);
        Tester tester = new Tester(commander, arguments, System.out);
        tester.process();
    }
}
