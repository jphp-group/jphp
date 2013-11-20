package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignShiftRightExprToken extends AssignOperatorExprToken {
    public AssignShiftRightExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SR_EQUAL);
    }
}
