package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.ExprToken;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

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
        Token token = rightValue.getLast();
        if (token instanceof ExprToken)
            return ((ExprToken) token).getLast();

        return token;
    }
}
