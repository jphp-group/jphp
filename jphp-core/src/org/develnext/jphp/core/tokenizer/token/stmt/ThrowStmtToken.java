package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class ThrowStmtToken extends StmtToken {
    protected ExprStmtToken exception;

    public ThrowStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_THROW);
    }

    public ExprStmtToken getException() {
        return exception;
    }

    public void setException(ExprStmtToken exception) {
        this.exception = exception;
    }
}
