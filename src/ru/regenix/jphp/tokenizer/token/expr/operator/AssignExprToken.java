package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

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
}
