package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignPlusExprToken extends AssignOperatorExprToken {
    public AssignPlusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_PLUS_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignPlus";
    }

    @Override
    public String getOperatorCode() {
        return "plus";
    }
}
