package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenType;

/**
 * :
 */
public class ColonToken extends Token {
    public ColonToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
