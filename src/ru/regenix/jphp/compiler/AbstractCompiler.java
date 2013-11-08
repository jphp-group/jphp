package ru.regenix.jphp.compiler;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.runtime.env.Environment;

import java.util.List;

abstract public class AbstractCompiler {

    protected final Environment environment;
    protected final CompileScope scope;
    protected final List<Token> tokens;
    protected final Context context;

    public AbstractCompiler(Environment environment, Context context, List<Token> tokens){
        this.tokens = tokens;
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

    abstract public void compile(boolean autoRegister);

    public void compile(){
        compile(true);
    }
}
