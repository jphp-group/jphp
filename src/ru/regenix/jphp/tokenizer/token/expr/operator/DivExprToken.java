package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class DivExprToken extends OperatorExprToken {
    public DivExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_DIV);
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
