package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class ArrayStmtToken extends StmtToken {
    public ArrayStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ARRAY);
    }
}
