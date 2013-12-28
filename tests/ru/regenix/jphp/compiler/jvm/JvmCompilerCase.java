package ru.regenix.jphp.compiler.jvm;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.runtime.ext.BCMathExtension;
import ru.regenix.jphp.runtime.ext.CTypeExtension;
import ru.regenix.jphp.runtime.ext.CoreExtension;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.io.File;
import java.util.List;

abstract public class JvmCompilerCase {
    protected Environment environment = new Environment();
    protected int runIndex = 0;
    protected String lastOutput;

    protected CompileScope newScope(){
        CompileScope compileScope = new CompileScope();
        compileScope.setDebugMode(true);

        compileScope.registerExtension(new CoreExtension());
        compileScope.registerExtension(new BCMathExtension());
        compileScope.registerExtension(new CTypeExtension());

        return compileScope;
    }

    protected List<Token> getSyntaxTree(Context context){
        Tokenizer tokenizer = new Tokenizer(context);
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
        return analyzer.getTree();
    }

    protected SyntaxAnalyzer getSyntax(Context context){
        Tokenizer tokenizer = new Tokenizer(context);
        return new SyntaxAnalyzer(tokenizer);
    }

    protected List<Token> getSyntaxTree(String code){
        return getSyntaxTree(new Context(environment, code));
    }

    protected Memory run(String code, boolean returned){
        runIndex += 1;
        Environment environment = new Environment(newScope());
        code = "class TestClass { static function test(){ " + (returned ? "return " : "") + code + "; } }";
        Context context = new Context(environment, code);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
        ModuleEntity module = compiler.compile();
        environment.getScope().loadModule(module);

        ClassEntity entity = module.findClass("TestClass");
        try {
            return entity.findMethod("test").invokeStatic(environment);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    protected Memory runDynamic(String code, boolean returned){
        runIndex += 1;
        Environment environment = new Environment(newScope());
        code = (returned ? "return " : "") + code + ";";
        Context context = new Context(environment, code);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
        ModuleEntity module = compiler.compile();
        environment.getScope().loadModule(module);
        environment.registerModule(module);

        return module.includeNoThrow(environment);
    }

    @SuppressWarnings("unchecked")
    protected Memory includeResource(String name, ArrayMemory globals){
        Environment environment = new Environment(newScope());
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("resources/" + name).getFile());
        Context context = new Context(environment, file);

        JvmCompiler compiler = new JvmCompiler(environment, context, getSyntax(context));
        ModuleEntity module = compiler.compile();
        environment.getScope().loadModule(module);
        environment.registerModule(module);

        if (globals != null)
            environment.getGlobals().putAll(globals);

        Memory memory = module.includeNoThrow(environment, environment.getGlobals());
        lastOutput = environment.getDefaultBuffer().getOutputAsString();
        return memory;
    }

    protected String getOutput(){
        return lastOutput;
    }

    protected Memory includeResource(String name){
        return includeResource(name, null);
    }

    protected Memory run(String code){
        return run(code, true);
    }

    protected Memory runDynamic(String code){
        return runDynamic(code, true);
    }
}
