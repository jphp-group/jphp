package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class VarStmtToken extends StmtToken {
    public VarStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_VAR);
    }
}
