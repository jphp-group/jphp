package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

abstract public class MacroToken extends ValueExprToken {
    public MacroToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
