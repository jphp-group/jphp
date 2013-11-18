package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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
