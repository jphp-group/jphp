package ru.regenix.jphp.lexer.tokens;


import ru.regenix.jphp.lexer.TokenType;

public class BreakToken extends Token {
    public BreakToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
