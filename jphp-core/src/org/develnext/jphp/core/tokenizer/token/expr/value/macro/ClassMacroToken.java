package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ClassMacroToken extends MacroToken {
    public ClassMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_CLASS_C);
    }
}
