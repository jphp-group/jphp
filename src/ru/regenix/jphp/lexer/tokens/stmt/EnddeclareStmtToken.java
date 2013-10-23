package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EnddeclareStmtToken extends EndStmtToken {
    public EnddeclareStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDDECLARE);
    }
}
