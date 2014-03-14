package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

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
