package ru.regenix.jphp.lexer.tokens.stmt;


import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class EndStmtToken extends StmtToken {
    public EndStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
