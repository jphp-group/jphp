package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class AssignExprToken extends OperatorExprToken
    implements AssignableOperatorToken {

    protected boolean asReference;

    public AssignExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_EQUAL);
    }

    @Override
    public int getPriority() {
        return 150;
    }

    public boolean isAsReference() {
        return asReference;
    }

    public void setAsReference(boolean asReference) {
        this.asReference = asReference;
    }

    @Override
    public boolean isRightSide() {
        return true;
    }

    @Override
    public String getCode() {
        return asReference ? "assignRef" : "assign";
    }
}
