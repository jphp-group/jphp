package ru.regenix.jphp.tokenizer.token;


import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class BreakToken extends Token {
    public BreakToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
