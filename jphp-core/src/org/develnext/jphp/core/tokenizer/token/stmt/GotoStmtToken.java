package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;

public class GotoStmtToken extends StmtToken {
    protected NameToken label;
    protected int level;

    public GotoStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_GOTO);
    }

    public NameToken getLabel() {
        return label;
    }

    public void setLabel(NameToken label) {
        this.label = label;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
