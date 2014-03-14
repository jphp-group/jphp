package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ArrayExprToken extends CallExprToken {
    public ArrayExprToken(TokenMeta meta) {
        super(meta);
        this.setName(new NameToken(meta));
    }
}
