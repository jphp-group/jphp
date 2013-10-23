package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EndforStmtToken extends EndStmtToken {
    public EndforStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDFOR);
    }
}
