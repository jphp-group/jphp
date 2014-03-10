package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class ProtectedStmtToken extends StmtToken {
    public ProtectedStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PROTECTED);
    }
}
