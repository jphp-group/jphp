package ru.regenix.jphp.compiler;

import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.Token;

import java.util.List;

abstract public class AbstractCompiler {

    protected final Environment environment;
    protected final List<Token> tokens;

    public AbstractCompiler(Environment environment, List<Token> tokens){
        this.environment = environment;
        this.tokens = tokens;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
