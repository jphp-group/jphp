package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class ClosureStmtToken extends StmtToken {
    private boolean returnReference;
    private List<ArgumentStmtToken> arguments;
    private List<ArgumentStmtToken> uses;

    private BodyStmtToken body;

    public ClosureStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public List<ArgumentStmtToken> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgumentStmtToken> arguments) {
        this.arguments = arguments;
    }

    public List<ArgumentStmtToken> getUses() {
        return uses;
    }

    public void setUses(List<ArgumentStmtToken> uses) {
        this.uses = uses;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    public boolean isReturnReference() {
        return returnReference;
    }

    public void setReturnReference(boolean returnReference) {
        this.returnReference = returnReference;
    }
}
