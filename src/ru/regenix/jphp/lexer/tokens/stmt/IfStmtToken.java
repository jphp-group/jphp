package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;

import java.util.Set;

public class IfStmtToken extends StmtToken {
    private Set<VariableExprToken> local;

    private ExprStmtToken condition;
    private BodyStmtToken body;
    private BodyStmtToken elseBody;

    public IfStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_IF);
    }

    public ExprStmtToken getCondition() {
        return condition;
    }

    public void setCondition(ExprStmtToken condition) {
        this.condition = condition;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    public BodyStmtToken getElseBody() {
        return elseBody;
    }

    public void setElseBody(BodyStmtToken elseBody) {
        this.elseBody = elseBody;
    }

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }
}
