package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class DirMacroToken extends MacroToken {
    public DirMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_DIR);
    }
}
