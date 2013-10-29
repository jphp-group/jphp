package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

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
