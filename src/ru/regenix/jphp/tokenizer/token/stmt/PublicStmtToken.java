package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class PublicStmtToken extends StmtToken {
    public PublicStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PUBLIC);
    }
}
