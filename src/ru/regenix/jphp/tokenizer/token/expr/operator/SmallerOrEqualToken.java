package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class SmallerOrEqualToken extends OperatorExprToken {
    public SmallerOrEqualToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_SMALLER_OR_EQUAL);
    }

    @Override
    public int getPriority() {
        return 70;
    }
}
