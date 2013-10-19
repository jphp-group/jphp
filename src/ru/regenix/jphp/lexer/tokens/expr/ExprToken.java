package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class ExprToken extends Token {
    public ExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public int getPriority(){
        return 0;
    }
}
