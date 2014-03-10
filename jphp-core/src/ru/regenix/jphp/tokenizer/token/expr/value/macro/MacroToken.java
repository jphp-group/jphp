package ru.regenix.jphp.tokenizer.token.expr.value.macro;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

abstract public class MacroToken extends ValueExprToken {
    public MacroToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
