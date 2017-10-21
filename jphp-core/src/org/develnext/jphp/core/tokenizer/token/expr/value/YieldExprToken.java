package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class YieldExprToken extends ValueExprToken implements CallableExprToken {
    protected ExprStmtToken value;
    protected boolean onlyGet;
    protected boolean onlyNext;

    protected boolean delegating;

    public YieldExprToken(TokenMeta meta) {
        super(meta, TokenType.T_YIELD);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    public boolean isOnlyGet() {
        return onlyGet;
    }

    public void setOnlyGet(boolean onlyGet) {
        this.onlyGet = onlyGet;
    }

    public boolean isOnlyNext() {
        return onlyNext;
    }

    public void setOnlyNext(boolean onlyNext) {
        this.onlyNext = onlyNext;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }

    public boolean isDelegating() {
        return delegating;
    }

    public void setDelegating(boolean delegating) {
        this.delegating = delegating;
    }
}
