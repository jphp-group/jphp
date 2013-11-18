package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class EndwhileStmtToken extends EndStmtToken {
    public EndwhileStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDWHILE);
    }
}
