package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class LineMacroToken extends MacroToken {

    public LineMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_LINE);
    }
}
