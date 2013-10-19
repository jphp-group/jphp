package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class NamespaceMacroToken extends MacroToken {
    public NamespaceMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_C);
    }
}
