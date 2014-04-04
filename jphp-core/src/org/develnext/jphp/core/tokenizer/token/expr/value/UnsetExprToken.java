package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class UnsetExprToken extends CallExprToken {
    public UnsetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_UNSET);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
