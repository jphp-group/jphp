package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class TraitMacroToken extends MacroToken {
    public TraitMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_TRAIT_C);
    }
}
