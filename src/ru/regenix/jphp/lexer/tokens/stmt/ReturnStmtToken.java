package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ReturnStmtToken extends StmtToken {
    public ReturnStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_RETURN);
    }
}
