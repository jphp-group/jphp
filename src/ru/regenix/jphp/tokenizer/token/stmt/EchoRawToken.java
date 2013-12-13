package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class EchoRawToken extends StmtToken {
    protected boolean isShort = true;

    public EchoRawToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public boolean isShort() {
        return isShort;
    }

    public void setShort(boolean aShort) {
        isShort = aShort;
    }
}
