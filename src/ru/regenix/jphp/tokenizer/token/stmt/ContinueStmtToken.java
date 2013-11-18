package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class ContinueStmtToken extends JumpStmtToken {
    public ContinueStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CONTINUE);
    }
}
