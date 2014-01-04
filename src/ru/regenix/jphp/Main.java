package ru.regenix.jphp;

import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.DieException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.ext.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.opcode.ModuleOpcodePrinter;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.runtime.util.JVMStackTracer;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.tokenizer.Tokenizer;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Throwable {
        CompileScope scope = new CompileScope();
        Environment environment = null;
        try {
            scope.registerExtension(new CoreExtension());
            scope.registerExtension(new BCMathExtension());
            scope.registerExtension(new CTypeExtension());
            scope.registerExtension(new CalendarExtension());
            scope.registerExtension(new DateExtension());
            scope.registerExtension(new SPLExtension());

            environment = new Environment(scope, System.out);
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

        } catch (DieException e) {
            System.exit(e.getExitCode());
        } catch (ErrorException e){
            System.out.println();

            System.out.println("[" + e.getType().name() + "] " + e.getMessage());
            System.out.print("    at line " + (e.getTraceInfo().getStartLine() + 1));
            System.out.print(", position " + (e.getTraceInfo().getStartPosition() + 1));
            System.out.println();
            System.out.println("    in '" + e.getTraceInfo().getFileName() + "'");

            System.out.println();
            JVMStackTracer tracer = scope.getStackTracer(e);
            for(JVMStackTracer.Item el : tracer){
                System.out.println("\tat " + (el.isInternal() ? "" : "-> ") + el);
            }

            //e.printStackTrace();
        } finally {
            if (environment != null)
                environment.clear();
        }
    }
}
