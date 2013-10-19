package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class MacroToken extends Token {
    public MacroToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
