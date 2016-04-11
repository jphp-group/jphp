package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken;

public class NullExprToken extends FulledNameToken {
    public NullExprToken(TokenMeta meta) {
        super(meta);
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Object toNumeric() {
        return 0L;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean isProcessed(NamespaceUseStmtToken.UseType useType) {
        return true;
    }
}
