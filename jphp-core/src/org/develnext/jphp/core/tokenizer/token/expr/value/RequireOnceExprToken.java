package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class RequireOnceExprToken extends ImportExprToken {
    public RequireOnceExprToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE_ONCE);
    }

    @Override
    public String getCode() {
        return "requireOnce";
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
