package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ClassMacroToken extends MacroToken {
    public ClassMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_CLASS_C);
    }
}
