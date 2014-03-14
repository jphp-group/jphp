package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

abstract public class JumpStmtToken extends StmtToken {
    private int level = 1;

    public JumpStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
