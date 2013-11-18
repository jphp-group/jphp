package ru.regenix.jphp.tokenizer.token.stmt;


import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

abstract public class EndStmtToken extends StmtToken {
    public EndStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
