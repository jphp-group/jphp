package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class BooleanNotEqualExprToken extends OperatorExprToken {
    public BooleanNotEqualExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_NOT_EQUAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }
}
