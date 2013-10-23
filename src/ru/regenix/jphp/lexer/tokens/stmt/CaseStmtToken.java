package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class CaseStmtToken extends StmtToken {
    public CaseStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CASE);
    }
}
