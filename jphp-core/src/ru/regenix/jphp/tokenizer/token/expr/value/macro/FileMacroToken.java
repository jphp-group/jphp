package ru.regenix.jphp.tokenizer.token.expr.value.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class FileMacroToken extends MacroToken {
    public FileMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_FILE);
    }
}
