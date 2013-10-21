package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AbstractStmtToken extends StmtToken {
    public AbstractStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ABSTRACT);
    }
}
