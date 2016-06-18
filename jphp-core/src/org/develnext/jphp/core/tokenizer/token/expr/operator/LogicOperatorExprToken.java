package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class LogicOperatorExprToken extends OperatorExprToken {

    private ExprStmtToken rightValue;

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    public LogicOperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public ExprStmtToken getRightValue() {
        return rightValue;
    }

    public void setRightValue(ExprStmtToken rightValue) {
        this.rightValue = rightValue;
    }

    @Override
    public Token getLast() {
        if (rightValue == null) {
            return null;
        }

        Token token = rightValue.getLast();

        if (token instanceof ExprToken)
            return ((ExprToken) token).getLast();

        return token;
    }
}
