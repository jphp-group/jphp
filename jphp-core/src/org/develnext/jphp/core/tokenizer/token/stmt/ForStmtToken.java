package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

import java.util.List;
import java.util.Set;

public class ForStmtToken extends StmtToken {
    private Set<VariableExprToken> local;
    private Set<VariableExprToken> initLocal;
    private Set<VariableExprToken> iterationLocal;
    private List<ExprStmtToken> initExpr;
    private List<ExprStmtToken> conditionExpr;
    private List<ExprStmtToken> iterationExpr;

    private BodyStmtToken body;

    public ForStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FOR);
    }

    public List<ExprStmtToken> getInitExpr() {
        return initExpr;
    }

    public void setInitExpr(List<ExprStmtToken> initExpr) {
        this.initExpr = initExpr;
    }

    public List<ExprStmtToken> getConditionExpr() {
        return conditionExpr;
    }

    public void setConditionExpr(List<ExprStmtToken> conditionExpr) {
        this.conditionExpr = conditionExpr;
    }

    public List<ExprStmtToken> getIterationExpr() {
        return iterationExpr;
    }

    public void setIterationExpr(List<ExprStmtToken> iterationExpr) {
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

    public Set<VariableExprToken> getInitLocal() {
        return initLocal;
    }

    public void setInitLocal(Set<VariableExprToken> initLocal) {
        this.initLocal = initLocal;
    }

    public Set<VariableExprToken> getIterationLocal() {
        return iterationLocal;
    }

    public void setIterationLocal(Set<VariableExprToken> iterationLocal) {
        this.iterationLocal = iterationLocal;
    }
}
