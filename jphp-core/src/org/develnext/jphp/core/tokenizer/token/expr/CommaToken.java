package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class CommaToken extends ExprToken {
    public CommaToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
