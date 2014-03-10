package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class OpenTagToken extends Token {
    public OpenTagToken(TokenMeta meta) {
        super(meta, TokenType.T_OPEN_TAG);
    }
}
