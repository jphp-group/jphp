package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignShiftRightExprToken extends AssignOperatorExprToken {
    public AssignShiftRightExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SR_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitShr";
    }

    @Override
    public String getOperatorCode() {
        return "bitShr";
    }
}
