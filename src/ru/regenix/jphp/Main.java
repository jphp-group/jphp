package ru.regenix.jphp;

import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.BytecodePrettyPrinter;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.ext.BCMathExtension;
import ru.regenix.jphp.runtime.ext.CTypeExtension;
import ru.regenix.jphp.runtime.ext.CalendarExtension;
import ru.regenix.jphp.runtime.ext.CoreExtension;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, InstantiationException {
        try {
            CompileScope scope = new CompileScope();
            scope.registerExtension(new CoreExtension());
            scope.registerExtension(new BCMathExtension());
            scope.registerExtension(new CTypeExtension());
            scope.registerExtension(new CalendarExtension());

            Environment environment = new Environment(scope, System.out);
            Context context = environment.createContext(new File("scripts/main.php"));

            // compile
            Tokenizer tokenizer = new Tokenizer(context);
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
            AbstractCompiler compiler = new JvmCompiler(environment, context, analyzer);

            ModuleEntity module = compiler.compile();
            scope.loadModule(module);

            String[] ops = BytecodePrettyPrinter.getMethod(module.getData(), "__include",
                    Type.getMethodDescriptor(Type.getType(Memory.class),
                            Type.getType(Environment.class),
                            Type.getType(String.class),
                            Type.getType(Memory[].class),
                            Type.getType(ArrayMemory.class)
                    )
            );
            for (String op : ops)
                System.out.println(op);

            long t = System.currentTimeMillis();

            Memory result = module.includeNoThrow(environment);

            environment.flushAll();

            System.out.println();
            System.out.println(System.currentTimeMillis() - t);
            System.out.println("--------------------");
            System.out.println(result);

        } catch (ErrorException e){
            System.out.println();
            System.out.println("[" + e.getType().name() + "] " + e.getMessage());
            System.out.print("    at line " + (e.getTraceInfo().getStartLine() + 1));
            System.out.print(", position " + (e.getTraceInfo().getStartPosition() + 1));
            System.out.println();
            System.out.println("    in '" + e.getTraceInfo().getFileName() + "'");
        }
    }
}
