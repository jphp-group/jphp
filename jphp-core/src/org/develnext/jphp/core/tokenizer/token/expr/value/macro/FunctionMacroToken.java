package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class FunctionMacroToken extends MacroToken {
    public FunctionMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNC_C);
    }
}
