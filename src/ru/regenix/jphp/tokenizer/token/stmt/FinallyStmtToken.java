package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class FinallyStmtToken extends StmtToken {
    public FinallyStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_FINALLY);
    }
}
