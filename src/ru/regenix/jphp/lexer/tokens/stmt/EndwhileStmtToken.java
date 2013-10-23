package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EndwhileStmtToken extends EndStmtToken {
    public EndwhileStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDWHILE);
    }
}
