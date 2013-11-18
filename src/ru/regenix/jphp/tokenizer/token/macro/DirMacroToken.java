package ru.regenix.jphp.tokenizer.token.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class DirMacroToken extends MacroToken {
    public DirMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_DIR);
    }
}
