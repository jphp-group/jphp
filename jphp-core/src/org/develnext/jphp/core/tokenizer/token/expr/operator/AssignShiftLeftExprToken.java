package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignShiftLeftExprToken extends AssignOperatorExprToken {
    public AssignShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitShl";
    }

    @Override
    public String getOperatorCode() {
        return "bitShl";
    }
}
