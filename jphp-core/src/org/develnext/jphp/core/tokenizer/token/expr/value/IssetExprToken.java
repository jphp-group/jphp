package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class IssetExprToken extends CallExprToken {
    public IssetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_ISSET);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
