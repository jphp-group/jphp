package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class EndifStmtToken extends EndStmtToken {

    public EndifStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public EndifStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDIF);
    }
}
