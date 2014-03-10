package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class BooleanXorExprToken extends OperatorExprToken {
    public BooleanXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_XOR);
    }

    @Override
    public int getPriority() {
        return 180;
    }
}
