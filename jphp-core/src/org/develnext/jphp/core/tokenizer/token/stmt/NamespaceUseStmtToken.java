package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class NamespaceUseStmtToken extends StmtToken {

    private FulledNameToken name;
    private NameToken as;

    public NamespaceUseStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_USE);
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }

    public NameToken getAs() {
        return as;
    }

    public void setAs(NameToken as) {
        this.as = as;
    }
}
