package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

import java.util.Set;

public class DoStmtToken extends StmtToken {
    private Set<VariableExprToken> local;
    private ExprStmtToken condition;
    private BodyStmtToken body;

    public DoStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DO);
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

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }
}
