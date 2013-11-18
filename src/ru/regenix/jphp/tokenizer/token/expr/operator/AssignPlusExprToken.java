package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class AssignPlusExprToken extends AssignOperatorExprToken {
    public AssignPlusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_PLUS_EQUAL);
    }
}
