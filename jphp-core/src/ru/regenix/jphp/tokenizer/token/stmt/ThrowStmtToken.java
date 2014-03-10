package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

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
