package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignOrExprToken extends AssignOperatorExprToken {
    public AssignOrExprToken(TokenMeta meta) {
        super(meta, TokenType.T_OR_EQUAL);
    }
}
