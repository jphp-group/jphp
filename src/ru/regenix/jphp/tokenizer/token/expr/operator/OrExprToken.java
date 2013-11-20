package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class OrExprToken extends OperatorExprToken {
    public OrExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
