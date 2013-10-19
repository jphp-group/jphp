package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ProtectedStmtToken extends StmtToken {
    public ProtectedStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PROTECTED);
    }
}
