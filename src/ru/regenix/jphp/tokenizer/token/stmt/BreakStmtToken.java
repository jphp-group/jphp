package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class BreakStmtToken extends JumpStmtToken {
    public BreakStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_BREAK);
    }
}
