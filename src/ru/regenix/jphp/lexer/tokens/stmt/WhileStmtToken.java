package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;

import java.util.List;
import java.util.Set;

public class WhileStmtToken extends StmtToken {
    private Set<VariableExprToken> local;
    private ExprStmtToken condition;
    private BodyStmtToken body;
    private boolean isDo;

    public WhileStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_WHILE);
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

    public boolean isDo() {
        return isDo;
    }

    public void setDo(boolean aDo) {
        isDo = aDo;
    }

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }
}
