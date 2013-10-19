package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class StmtToken extends Token {

    public StmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
