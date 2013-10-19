package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class DoStmtToken extends StmtToken {
    public DoStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DO);
    }
}
