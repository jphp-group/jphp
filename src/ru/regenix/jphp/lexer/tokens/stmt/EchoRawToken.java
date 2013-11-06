package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class EchoRawToken extends StmtToken {
    public EchoRawToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
