package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

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
