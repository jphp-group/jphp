package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class NamespaceMacroToken extends MacroToken {
    public NamespaceMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_C);
    }
}
