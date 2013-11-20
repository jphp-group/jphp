package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class AtExprToken extends OperatorExprToken {
    public AtExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
