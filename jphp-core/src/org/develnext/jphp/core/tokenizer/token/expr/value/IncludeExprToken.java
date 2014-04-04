package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class IncludeExprToken extends ImportExprToken {
    public IncludeExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INCLUDE);
    }

    @Override
    public String getCode() {
        return "include";
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
