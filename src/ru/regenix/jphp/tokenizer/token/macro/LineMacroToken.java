package ru.regenix.jphp.tokenizer.token.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class LineMacroToken extends MacroToken {

    public LineMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_LINE);
    }
}
