package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

public class StaticExprToken extends ValueExprToken {
    public StaticExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STATIC);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
