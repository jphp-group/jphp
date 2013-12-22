package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class UsesStmtToken extends StmtToken {
    public UsesStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
    }
}
