package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class StringStartDocToken extends Token {
    public StringStartDocToken(TokenMeta meta) {
        super(meta, TokenType.T_START_HEREDOC);
    }
}
