package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class DefaultStmtToken extends CaseStmtToken {

    private BodyStmtToken body;

    public DefaultStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DEFAULT);
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }
}
