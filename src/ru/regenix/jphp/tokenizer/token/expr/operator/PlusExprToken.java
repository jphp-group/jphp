package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class PlusExprToken extends OperatorExprToken {
    public PlusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_PLUS);
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
