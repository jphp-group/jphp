package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignMinusExprToken extends AssignOperatorExprToken {
    public AssignMinusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MINUS_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignMinus";
    }

    @Override
    public String getOperatorCode() {
        return "minus";
    }
}
