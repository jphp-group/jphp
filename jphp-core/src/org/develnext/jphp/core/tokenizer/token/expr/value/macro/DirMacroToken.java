package org.develnext.jphp.core.tokenizer.token.expr.value.macro;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class DirMacroToken extends MacroToken {
    public DirMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_DIR);
    }
}
