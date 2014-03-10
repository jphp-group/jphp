package ru.regenix.jphp.tokenizer.token.expr.value.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class NamespaceMacroToken extends MacroToken {
    public NamespaceMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_C);
    }
}
