package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class FinalStmtToken extends StmtToken {
    public FinalStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FINAL);
    }
}
