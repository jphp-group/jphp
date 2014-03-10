package ru.regenix.jphp.tokenizer.token.expr.value.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class LineMacroToken extends MacroToken {

    public LineMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_LINE);
    }
}
