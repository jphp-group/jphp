package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EndifStmtToken extends EndStmtToken {
    public EndifStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDIF);
    }
}
