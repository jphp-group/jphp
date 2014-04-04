package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class RequireExprToken extends ImportExprToken {
    public RequireExprToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE);
    }

    @Override
    public String getCode() {
        return "require";
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
