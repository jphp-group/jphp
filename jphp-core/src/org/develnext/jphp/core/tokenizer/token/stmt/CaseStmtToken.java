package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

import java.util.Set;

public class CaseStmtToken extends StmtToken {
    private Set<VariableExprToken> locals;
    private ExprStmtToken conditional;
    private BodyStmtToken body;

    protected CaseStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public CaseStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CASE);
    }

    public ExprStmtToken getConditional() {
        return conditional;
    }

    public void setConditional(ExprStmtToken conditional) {
        this.conditional = conditional;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    public Set<VariableExprToken> getLocals() {
        return locals;
    }

    public void setLocals(Set<VariableExprToken> locals) {
        this.locals = locals;
    }
}
