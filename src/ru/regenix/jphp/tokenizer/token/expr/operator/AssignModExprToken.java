package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignModExprToken extends AssignOperatorExprToken {
    public AssignModExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MOD_EQUAL);
    }
}
