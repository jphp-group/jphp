package ru.regenix.jphp.compiler.jvm;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

abstract public class JvmCompilerCase {
    protected Environment environment = new Environment();
    protected Context context = new Context(environment, new File("test.php"));

    protected int runIndex = 0;

    protected List<Token> getSyntaxTree(String code){
        Tokenizer tokenizer = new Tokenizer(context, code);
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
        return analyzer.getTree();
    }

    protected Memory run(String code, boolean returned){
        runIndex += 1;
        code = "class TestClass { static function test(){ " + (returned ? "return " : "") + code + "; } }";

        JvmCompiler compiler = new JvmCompiler(new CompileScope(), context, getSyntaxTree(code));
        compiler.compile();

        MyClassLoader classLoader = new MyClassLoader(Thread.currentThread().getContextClassLoader());
        try {
            Class clazz = classLoader.loadClass("TestClass", compiler.getClasses().get(0).getCw().toByteArray());
            Method method = clazz.getMethod("test", Environment.class, Memory[].class);
            return (Memory) method.invoke(null, environment, new Memory[]{});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected Memory run(String code){
        return run(code, true);
    }
}
