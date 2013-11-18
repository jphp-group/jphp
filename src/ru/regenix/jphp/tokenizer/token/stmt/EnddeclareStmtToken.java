package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class EnddeclareStmtToken extends EndStmtToken {
    public EnddeclareStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDDECLARE);
    }
}
