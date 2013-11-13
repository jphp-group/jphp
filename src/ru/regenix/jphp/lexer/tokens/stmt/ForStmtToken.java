package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;

import java.util.Set;

public class ForStmtToken extends StmtToken {
    private Set<VariableExprToken> local;
    private ExprStmtToken initExpr;
    private ExprStmtToken condition;
    private ExprStmtToken iterationExpr;

    private BodyStmtToken body;

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

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }
}
