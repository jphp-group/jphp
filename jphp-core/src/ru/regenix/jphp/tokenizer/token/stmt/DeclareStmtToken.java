package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class DeclareStmtToken extends StmtToken {
    public DeclareStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DECLARE);
    }
}
