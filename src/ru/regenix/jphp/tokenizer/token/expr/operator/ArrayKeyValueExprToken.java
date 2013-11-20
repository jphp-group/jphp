package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class ArrayKeyValueExprToken extends OperatorExprToken {
    public ArrayKeyValueExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DOUBLE_ARROW);
    }
}
