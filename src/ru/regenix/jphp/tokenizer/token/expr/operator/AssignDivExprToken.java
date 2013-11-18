package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class AssignDivExprToken extends AssignOperatorExprToken {
    public AssignDivExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DIV_EQUAL);
    }
}
