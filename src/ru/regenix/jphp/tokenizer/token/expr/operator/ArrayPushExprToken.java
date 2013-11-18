package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class ArrayPushExprToken extends OperatorExprToken {
    public ArrayPushExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_ARRAY_PUSH);
    }
}
