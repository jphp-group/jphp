package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class IncExprToken extends OperatorExprToken {
    public IncExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INC);
    }

    @Override
    public int getPriority() {
        return 21;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String getCode() {
        return "inc";
    }
}
