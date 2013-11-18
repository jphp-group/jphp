package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

public class LogicOperatorExprToken extends OperatorExprToken {

    private ExprStmtToken rightValue;

    public LogicOperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public ExprStmtToken getRightValue() {
        return rightValue;
    }

    public void setRightValue(ExprStmtToken rightValue) {
        this.rightValue = rightValue;
    }
}
