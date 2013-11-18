package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class EndswitchStmtToken extends EndStmtToken {

    public EndswitchStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public EndswitchStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDSWITCH);
    }
}
