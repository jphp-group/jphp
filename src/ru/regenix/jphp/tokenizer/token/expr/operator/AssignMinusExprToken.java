package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class AssignMinusExprToken extends AssignOperatorExprToken {
    public AssignMinusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MINUS_EQUAL);
    }
}
