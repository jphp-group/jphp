package ru.regenix.jphp;

import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.BytecodePrettyPrinter;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.runtime.memory.ArrayMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.compiler.jvm.runtime.type.FastIntMap;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Environment environment = new Environment();
        Context context;

        Tokenizer tokenizer = new Tokenizer(context = new Context(environment,
                        "class MyClass { " +
                                "static function test(){ " +
                                    " $i = -1; while($i += 1 < 1000000){ $arr[$i] = 20; } " +
                                "} " +
                        "}"));

        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
        JvmCompiler compiler = new JvmCompiler(new CompileScope(), context, analyzer.getTree());

        MyClassLoader classLoader = new MyClassLoader();
        compiler.compile();

        Thread.currentThread().setContextClassLoader(classLoader);
        Class<?> clazz = classLoader.loadClass("MyClass", compiler.getClasses().get(0).getCw().toByteArray());

        String[] ops = BytecodePrettyPrinter.getMethod(compiler.getClasses().get(0).getCw(), "test",
                Type.getMethodDescriptor(Type.getType(Memory.class), Type.getType(Environment.class),
                        Type.getType(Memory[].class)
                )
        );

        for (String op : ops)
            System.out.println(op);

        Method method = clazz.getMethod("test", Environment.class, Memory[].class);
        long t = System.currentTimeMillis();
        FastIntMap<Integer> map = new FastIntMap<Integer>();
        Map<String, Integer> slowMap = new LinkedHashMap<String, Integer>();
        Memory table = new ArrayMemory();

        /*for(int i = 0; i < 1000; i++){
            table.valueOfIndex(i + "x").assign(i);
            //map.put(i, i);
            //slowMap.put(i + "x", i);
        }

        for(int j = 0; j < 10000;j++){
            for(int i = 0; i < 1000; i++){
                table.valueOfIndex(i + "x");
                //map.get(i);
                //slowMap.get(i + "x");
            }
        }*/

        Memory memory = (Memory) method.invoke(null, environment, new Memory[]{});
        //System.out.println(memory.toString());
        System.out.println(System.currentTimeMillis() - t);
    }
}
