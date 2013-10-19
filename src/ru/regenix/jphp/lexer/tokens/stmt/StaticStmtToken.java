package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class StaticStmtToken extends StmtToken {
    public StaticStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_STATIC);
    }
}
