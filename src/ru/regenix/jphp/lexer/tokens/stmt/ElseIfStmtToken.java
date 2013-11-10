package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ElseIfStmtToken extends EndifStmtToken {
    public ElseIfStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ELSEIF);
    }
}
