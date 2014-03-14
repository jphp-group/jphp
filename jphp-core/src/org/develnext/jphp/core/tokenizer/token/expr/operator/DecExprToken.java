package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class DecExprToken extends OperatorExprToken {
    public DecExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DEC);
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
        return "dec";
    }
}
