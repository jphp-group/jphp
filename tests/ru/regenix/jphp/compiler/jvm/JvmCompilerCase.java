package ru.regenix.jphp.compiler.jvm;

import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.List;

abstract public class JvmCompilerCase {
    protected Environment environment = new Environment();
    protected int runIndex = 0;

    protected List<Token> getSyntaxTree(Context context){
        Tokenizer tokenizer = new Tokenizer(context);
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
        return analyzer.getTree();
    }

    protected List<Token> getSyntaxTree(String code){
        return getSyntaxTree(new Context(environment, code));
    }

    protected Memory run(String code, boolean returned){
        runIndex += 1;
        Environment environment = new Environment();
        code = "class TestClass { static function test(){ " + (returned ? "return " : "") + code + "; } }";
        Context context = new Context(environment, code);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntaxTree(context));
        compiler.compile();

        ClassEntity entity = environment.getScope().loadClass("TestClass");
        return entity.findMethod("test").invoke(environment);
    }

    protected Memory run(String code){
        return run(code, true);
    }
}
