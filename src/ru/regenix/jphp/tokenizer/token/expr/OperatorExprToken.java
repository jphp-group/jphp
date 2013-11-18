package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

abstract public class OperatorExprToken extends ExprToken {

    public OperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public boolean isBinary(){
        return true;
    }
}
