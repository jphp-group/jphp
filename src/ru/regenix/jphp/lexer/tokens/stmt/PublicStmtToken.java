package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class PublicStmtToken extends StmtToken {
    public PublicStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PUBLIC);
    }
}
