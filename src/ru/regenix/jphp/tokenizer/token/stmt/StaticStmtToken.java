package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class StaticStmtToken extends StmtToken {
    public StaticStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_STATIC);
    }
}
