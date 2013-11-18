package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class AssignShiftLeftExprToken extends AssignOperatorExprToken {
    public AssignShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL_EQUAL);
    }
}
