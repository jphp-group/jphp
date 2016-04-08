package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken;

public class BooleanExprToken extends FulledNameToken {

    private boolean value;

    public BooleanExprToken(TokenMeta meta) {
        super(meta);
        String word = meta.getWord().toLowerCase();
        if ("true".equals(word))
            this.value = true;
        else if ("false".equals(word))
            this.value = false;
        else
            throw new IllegalArgumentException("Word must be TRUE of FALSE");
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Object toNumeric() {
        return value ? 1L : 0L;
    }

    @Override
    public String toString() {
        return value ? "1" : "";
    }

    @Override
    public boolean isProcessed(NamespaceUseStmtToken.UseType useType) {
        return true;
    }
}
