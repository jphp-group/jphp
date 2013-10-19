package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class TryStmtToken extends StmtToken {
    public TryStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_TRY);
    }
}
