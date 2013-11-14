package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class BreakStmtToken extends JumpStmtToken {
    public BreakStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_BREAK);
    }
}
