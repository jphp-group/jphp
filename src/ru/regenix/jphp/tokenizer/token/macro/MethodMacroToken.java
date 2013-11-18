package ru.regenix.jphp.tokenizer.token.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class MethodMacroToken extends MacroToken {
    public MethodMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_METHOD_C);
    }
}
