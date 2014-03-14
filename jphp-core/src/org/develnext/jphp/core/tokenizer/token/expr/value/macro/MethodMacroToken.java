package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class MethodMacroToken extends MacroToken {
    public MethodMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_METHOD_C);
    }
}
