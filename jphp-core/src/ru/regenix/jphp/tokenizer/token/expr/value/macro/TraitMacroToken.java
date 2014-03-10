package ru.regenix.jphp.tokenizer.token.expr.value.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class TraitMacroToken extends MacroToken {
    public TraitMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_TRAIT_C);
    }
}
