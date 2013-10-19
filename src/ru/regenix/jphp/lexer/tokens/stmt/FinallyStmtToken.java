package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class FinallyStmtToken extends StmtToken {
    public FinallyStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FINAL);
    }
}
