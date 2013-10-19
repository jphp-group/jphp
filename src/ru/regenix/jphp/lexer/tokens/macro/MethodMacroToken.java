package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class MethodMacroToken extends MacroToken {
    public MethodMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_METHOD_C);
    }
}
