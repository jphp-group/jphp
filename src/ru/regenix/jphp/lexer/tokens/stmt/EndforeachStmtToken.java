package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EndforeachStmtToken extends EndStmtToken {
    public EndforeachStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDFOREACH);
    }
}
