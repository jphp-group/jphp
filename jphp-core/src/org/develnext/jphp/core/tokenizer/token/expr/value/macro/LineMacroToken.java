package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class LineMacroToken extends MacroToken {

    public LineMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_LINE);
    }
}
