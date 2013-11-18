package ru.regenix.jphp.tokenizer.token.macro;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class NamespaceMacroToken extends MacroToken {
    public NamespaceMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_C);
    }
}
