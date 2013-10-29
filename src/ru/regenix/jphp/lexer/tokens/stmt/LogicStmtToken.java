package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class LogicStmtToken extends StmtToken {

    public ExprStmtToken condition;
    public ExprStmtToken nextExpression;

    public LogicStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public ExprStmtToken getCondition() {
        return condition;
    }

    public void setCondition(ExprStmtToken condition) {
        this.condition = condition;
    }

    public ExprStmtToken getNextExpression() {
        return nextExpression;
    }

    public void setNextExpression(ExprStmtToken nextExpression) {
        this.nextExpression = nextExpression;
    }
}
