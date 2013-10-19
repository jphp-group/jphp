package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ForStmtToken extends StmtToken {
    private ExprStmtToken initExpr;
    private ExprStmtToken condition;
    private ExprStmtToken iterationExpr;

    public ForStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FOR);
    }

    public ExprStmtToken getInitExpr() {
        return initExpr;
    }

    public void setInitExpr(ExprStmtToken initExpr) {
        this.initExpr = initExpr;
    }

    public ExprStmtToken getCondition() {
        return condition;
    }

    public void setCondition(ExprStmtToken condition) {
        this.condition = condition;
    }

    public ExprStmtToken getIterationExpr() {
        return iterationExpr;
    }

    public void setIterationExpr(ExprStmtToken iterationExpr) {
        this.iterationExpr = iterationExpr;
    }
}
