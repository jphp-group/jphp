package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

public class ThisExprToken extends ValueExprToken {
    public ThisExprToken(TokenMeta meta) {
        super(meta, TokenType.T_VARIABLE);
    }
}
