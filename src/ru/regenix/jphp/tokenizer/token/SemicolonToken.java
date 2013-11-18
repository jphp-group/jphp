package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenType;

public class SemicolonToken extends Token {

    public SemicolonToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
