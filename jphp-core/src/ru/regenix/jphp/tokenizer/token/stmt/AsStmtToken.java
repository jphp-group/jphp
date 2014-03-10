package ru.regenix.jphp.tokenizer.token.stmt;


import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AsStmtToken extends StmtToken {
    public AsStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_AS);
    }
}
