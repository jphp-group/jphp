package ru.regenix.jphp.compiler;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.List;

abstract public class AbstractCompiler {

    protected final Environment environment;
    protected final CompileScope scope;
    protected final SyntaxAnalyzer analyzer;
    protected final List<Token> tokens;
    protected final Context context;

    public AbstractCompiler(Environment environment, Context context, SyntaxAnalyzer analyzer){
        this.tokens = analyzer.getTree();
        this.analyzer = analyzer;
        this.context = context;
        this.scope = environment.getScope();
        this.environment = environment;
    }

    public Context getContext() {
        return context;
    }

    public CompileScope getScope() {
        return scope;
    }

    abstract public ModuleEntity compile(boolean autoRegister);

    public ModuleEntity compile(){
        return compile(true);
    }
}
