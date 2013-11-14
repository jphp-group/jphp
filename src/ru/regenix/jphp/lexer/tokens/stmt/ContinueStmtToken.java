package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ContinueStmtToken extends JumpStmtToken {
    public ContinueStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CONTINUE);
    }
}
