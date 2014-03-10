package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

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
