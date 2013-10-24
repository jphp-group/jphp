package ru.regenix.jphp.compiler.jvm;

import ru.regenix.jphp.compiler.AbstractCompiler;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.Token;

import java.util.List;

public class JvmCompiler extends AbstractCompiler {

    public JvmCompiler(Environment environment, List<Token> tokens) {
        super(environment, tokens);
    }
}
