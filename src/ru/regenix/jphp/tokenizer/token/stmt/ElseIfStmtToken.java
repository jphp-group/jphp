package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class ElseIfStmtToken extends EndifStmtToken {
    public ElseIfStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ELSEIF);
    }
}
