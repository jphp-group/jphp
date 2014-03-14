package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

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
