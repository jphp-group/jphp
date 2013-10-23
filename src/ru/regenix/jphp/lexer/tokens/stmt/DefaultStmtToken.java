package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class DefaultStmtToken extends StmtToken {
    public DefaultStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DEFAULT);
    }
}
