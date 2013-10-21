package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class FinalStmtToken extends StmtToken {
    public FinalStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FINAL);
    }
}
