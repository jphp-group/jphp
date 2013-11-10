package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class OperatorExprToken extends ExprToken {

    public OperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public boolean isBinary(){
        return true;
    }
}
