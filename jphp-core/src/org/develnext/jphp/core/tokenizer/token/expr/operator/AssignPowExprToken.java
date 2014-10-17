package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class AssignPowExprToken extends AssignOperatorExprToken {
    public AssignPowExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_POW_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignPow";
    }

    @Override
    public String getOperatorCode() {
        return "pow";
    }
}
