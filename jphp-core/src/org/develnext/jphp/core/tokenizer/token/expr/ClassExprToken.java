package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class ClassExprToken extends ValueExprToken {
    public ClassExprToken(TokenMeta meta) {
        super(meta, TokenType.T_CLASS);
    }
}
