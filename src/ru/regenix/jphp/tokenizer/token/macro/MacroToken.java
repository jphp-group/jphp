package ru.regenix.jphp.tokenizer.token.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

abstract public class MacroToken extends Token {
    public MacroToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
