package ru.regenix.jphp;

import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.ext.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.opcode.ModuleOpcodePrinter;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.tokenizer.Tokenizer;

import java.io.File;

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

            System.out.println(new ModuleOpcodePrinter(module).toString());

            scope.loadModule(module);
            environment.registerModule(module);

            long t = System.currentTimeMillis();
            Memory result = module.includeNoThrow(environment);

            environment.flushAll();

            System.out.println();
            System.out.println(System.currentTimeMillis() - t);
            System.out.println("--------------------");
            System.out.println(result);

        } catch (Exception e) {
            environment.catchUncaught(e);
        } finally {
            environment.doFinal();
        }
    }
}
