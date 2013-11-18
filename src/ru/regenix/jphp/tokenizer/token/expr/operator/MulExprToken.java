package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class MulExprToken extends OperatorExprToken {
    public MulExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MUL);
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
