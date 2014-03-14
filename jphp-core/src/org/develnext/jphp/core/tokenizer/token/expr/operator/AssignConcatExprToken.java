package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AssignConcatExprToken extends AssignOperatorExprToken {
    public AssignConcatExprToken(TokenMeta meta) {
        super(meta, TokenType.T_CONCAT_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignConcat";
    }

    @Override
    public String getOperatorCode() {
        return "concat";
    }
}
