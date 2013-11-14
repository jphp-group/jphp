package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class CaseStmtToken extends StmtToken {

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
}
