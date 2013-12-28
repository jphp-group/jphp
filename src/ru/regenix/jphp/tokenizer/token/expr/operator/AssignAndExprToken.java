package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignAndExprToken extends AssignOperatorExprToken {
    public AssignAndExprToken(TokenMeta meta) {
        super(meta, TokenType.T_AND_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitAnd";
    }

    @Override
    public String getOperatorCode() {
        return "bitAnd";
    }
}
