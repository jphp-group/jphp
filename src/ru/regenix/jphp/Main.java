package ru.regenix.jphp;

import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.BytecodePrettyPrinter;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.ext.CoreExtension;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, InstantiationException {
        try {
            CompileScope scope = new CompileScope();
            scope.registerExtension(new CoreExtension());

            Environment environment = new Environment(scope, System.out);
            Context context = environment.createContext(new File("main.php"));

            // compile
            Tokenizer tokenizer = new Tokenizer(context);
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
            AbstractCompiler compiler = new JvmCompiler(environment, context, analyzer.getTree());
            compiler.compile();

            ClassEntity myClass = scope.loadClass("MyClass");

            String[] ops = BytecodePrettyPrinter.getMethod(myClass, "test",
                    Type.getMethodDescriptor(Type.getType(Memory.class), Type.getType(Environment.class),
                            Type.getType(Memory[].class)
                    )
            );
            for (String op : ops)
                System.out.println(op);

            long t = System.currentTimeMillis();
            Memory result = myClass.findMethod("test").invoke(environment);
            environment.flushAll();

            System.out.println();
            System.out.println(System.currentTimeMillis() - t);
            System.out.println("--------------------");

        } catch (ErrorException e){
            System.out.println("[" + e.getType().name() + "] " + e.getMessage());
            System.out.print("    at line " + e.getTraceInfo().getStartLine());
            System.out.print(", position " + e.getTraceInfo().getStartPosition());
        }
    }
}
