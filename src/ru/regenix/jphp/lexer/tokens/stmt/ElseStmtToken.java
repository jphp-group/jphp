package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ElseStmtToken extends StmtToken {
    public ElseStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ELSE);
    }
}
