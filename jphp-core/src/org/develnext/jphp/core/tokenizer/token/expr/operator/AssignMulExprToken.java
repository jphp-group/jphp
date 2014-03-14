package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignMulExprToken extends AssignOperatorExprToken {
    public AssignMulExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MUL_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignMul";
    }

    @Override
    public String getOperatorCode() {
        return "mul";
    }
}
