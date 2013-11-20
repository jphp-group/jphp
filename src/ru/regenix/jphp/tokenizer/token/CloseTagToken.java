package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class CloseTagToken extends Token {
    public CloseTagToken(TokenMeta meta) {
        super(meta, TokenType.T_CLOSE_TAG);
    }
}
