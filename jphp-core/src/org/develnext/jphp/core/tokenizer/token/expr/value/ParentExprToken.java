package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ParentExprToken extends FulledNameToken {
    public ParentExprToken(TokenMeta meta) {
        super(meta);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }

    @Override
    public boolean isProcessed() {
        return true;
    }
}
