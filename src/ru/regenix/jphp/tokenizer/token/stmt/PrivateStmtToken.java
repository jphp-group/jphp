package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class PrivateStmtToken extends StmtToken {
    public PrivateStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PRIVATE);
    }
}
