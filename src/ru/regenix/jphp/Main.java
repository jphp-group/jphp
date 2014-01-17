package ru.regenix.jphp;

import php.runtime.Memory;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.*;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.opcode.ModuleOpcodePrinter;
import php.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.tokenizer.Tokenizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) throws Throwable {
        CompileScope scope = new CompileScope();
        scope.setDebugMode(true);
        scope.registerExtension(new CoreExtension());
        scope.registerExtension(new BCMathExtension());
        scope.registerExtension(new CTypeExtension());
        scope.registerExtension(new CalendarExtension());
        scope.registerExtension(new DateExtension());
        scope.registerExtension(new SPLExtension());

        Environment environment = new Environment(scope, System.out);
        try {
            Context context = environment.createContext(new File("scripts/main.php"));

            // compile
            Tokenizer tokenizer = new Tokenizer(context);
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);
            AbstractCompiler compiler = new JvmCompiler(environment, context, analyzer);
            ModuleEntity module = compiler.compile();

            ModuleDumper dumper = new ModuleDumper(context, environment, true);
            dumper.save(module, new FileOutputStream("scripts/main.phb"));
            module = dumper.load(new FileInputStream("scripts/main.phb"));

            scope.loadModule(module);

            System.out.println(new ModuleOpcodePrinter(module).toString());

            environment.registerModule(module);

            long t = System.currentTimeMillis();

            Memory result = module.includeNoThrow(environment);

            environment.flushAll();

            System.out.println();
            System.out.println(System.currentTimeMillis() - t);
            System.out.println("--------------------");
            //System.out.println(result);

        } catch (Exception e) {
            environment.catchUncaught(e);
        } finally {
            environment.doFinal();
        }
    }
}
