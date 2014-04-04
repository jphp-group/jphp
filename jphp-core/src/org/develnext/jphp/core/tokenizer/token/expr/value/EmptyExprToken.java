package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class EmptyExprToken extends ValueExprToken implements CallableExprToken {
    protected ExprStmtToken value;

    public EmptyExprToken(TokenMeta meta) {
        super(meta, TokenType.T_EMPTY);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
