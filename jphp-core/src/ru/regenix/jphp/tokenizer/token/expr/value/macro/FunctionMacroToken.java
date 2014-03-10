package ru.regenix.jphp.tokenizer.token.expr.value.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class FunctionMacroToken extends MacroToken {
    public FunctionMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNC_C);
    }
}
