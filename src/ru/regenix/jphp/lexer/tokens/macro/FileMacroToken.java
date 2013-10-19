package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class FileMacroToken extends MacroToken {
    public FileMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_FILE);
    }
}
