package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignMulExprToken extends AssignOperatorExprToken {
    public AssignMulExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MUL_EQUAL);
    }
}
