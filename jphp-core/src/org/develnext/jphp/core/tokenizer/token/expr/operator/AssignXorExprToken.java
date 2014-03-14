package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignXorExprToken extends AssignOperatorExprToken {
    public AssignXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_XOR_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitXor";
    }

    @Override
    public String getOperatorCode() {
        return "bitXor";
    }
}
