package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class IncludeOnceExprToken extends ImportExprToken {
    public IncludeOnceExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INCLUDE_ONCE);
    }

    @Override
    public String getCode() {
        return "includeOnce";
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
