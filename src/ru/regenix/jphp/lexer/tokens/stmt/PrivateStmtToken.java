package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class PrivateStmtToken extends StmtToken {
    public PrivateStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PRIVATE);
    }
}
