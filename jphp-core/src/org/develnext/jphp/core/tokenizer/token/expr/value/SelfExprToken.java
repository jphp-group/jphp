package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

public class SelfExprToken extends ValueExprToken {
    public SelfExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
