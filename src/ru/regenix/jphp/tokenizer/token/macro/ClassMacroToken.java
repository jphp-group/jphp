package ru.regenix.jphp.tokenizer.token.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class ClassMacroToken extends MacroToken {
    public ClassMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_CLASS_C);
    }
}
