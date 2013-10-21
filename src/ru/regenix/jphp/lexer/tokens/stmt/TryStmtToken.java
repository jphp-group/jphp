package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class TryStmtToken extends StmtToken {

    private BodyStmtToken body;

    public TryStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_TRY);
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }
}
