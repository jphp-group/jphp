package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class AssignNullCoalesceIfElseToken extends AssignOperatorExprToken {
    public AssignNullCoalesceIfElseToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public String getCode() {
        return "assignCoalesceIfElse";
    }

    @Override
    public String getOperatorCode() {
        return "valueCoalesceIfElse";
    }
}
