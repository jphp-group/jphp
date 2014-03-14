package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class BackslashExprToken extends ExprToken {
    public BackslashExprToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_SEPARATOR);
    }
}
