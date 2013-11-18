package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

abstract public class StmtToken extends Token {

    public StmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
