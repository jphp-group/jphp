package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class EndforeachStmtToken extends EndStmtToken {
    public EndforeachStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDFOREACH);
    }
}
