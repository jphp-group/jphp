package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignOrExprToken extends AssignOperatorExprToken {
    public AssignOrExprToken(TokenMeta meta) {
        super(meta, TokenType.T_OR_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitOr";
    }

    @Override
    public String getOperatorCode() {
        return "bitOr";
    }
}
