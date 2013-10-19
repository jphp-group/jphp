package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class StmtToken extends Token {

    protected StmtToken(TokenMeta meta) {
        super(meta);
    }
}
