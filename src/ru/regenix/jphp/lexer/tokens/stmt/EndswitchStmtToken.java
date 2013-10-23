package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EndswitchStmtToken extends EndStmtToken {
    public EndswitchStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDSWITCH);
    }
}
