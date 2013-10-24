package ru.regenix.jphp.compiler;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.Token;

import java.util.List;

abstract public class AbstractCompiler {

    protected final Environment environment;
    protected final List<Token> tokens;
    protected final Context context;

    public AbstractCompiler(Environment environment, Context context, List<Token> tokens){
        this.environment = environment;
        this.tokens = tokens;
        this.context = context;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Context getContext() {
        return context;
    }
}
