package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignModExprToken extends AssignOperatorExprToken {
    public AssignModExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MOD_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignMod";
    }

    @Override
    public String getOperatorCode() {
        return "mod";
    }
}
