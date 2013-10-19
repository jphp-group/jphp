package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class TraitMacroToken extends MacroToken {
    public TraitMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_TRAIT_C);
    }
}
