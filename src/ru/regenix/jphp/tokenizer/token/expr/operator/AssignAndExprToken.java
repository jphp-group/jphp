package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class AssignAndExprToken extends AssignOperatorExprToken {
    public AssignAndExprToken(TokenMeta meta) {
        super(meta, TokenType.T_AND_EQUAL);
    }
}
