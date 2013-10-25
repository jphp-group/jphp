package ru.regenix.jphp.compiler;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.lexer.tokens.Token;

import java.util.List;

abstract public class AbstractCompiler {

    protected final CompileScope scope;
    protected final List<Token> tokens;
    protected final Context context;

    public AbstractCompiler(CompileScope scope, Context context, List<Token> tokens){
        this.tokens = tokens;
        this.context = context;
        this.scope = scope;
    }

    public Context getContext() {
        return context;
    }

    public CompileScope getScope() {
        return scope;
    }
}
