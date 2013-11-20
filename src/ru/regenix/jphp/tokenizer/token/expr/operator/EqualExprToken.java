package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class EqualExprToken extends OperatorExprToken {
    public EqualExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_EQUAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }
}
