package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class AbstractStmtToken extends StmtToken {
    public AbstractStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ABSTRACT);
    }
}
