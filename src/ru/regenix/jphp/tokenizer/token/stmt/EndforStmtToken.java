package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class EndforStmtToken extends EndStmtToken {
    public EndforStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDFOR);
    }
}
