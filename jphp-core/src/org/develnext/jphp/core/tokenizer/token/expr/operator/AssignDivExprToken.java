package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignDivExprToken extends AssignOperatorExprToken {
    public AssignDivExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DIV_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignDiv";
    }

    @Override
    public String getOperatorCode() {
        return "div";
    }
}
