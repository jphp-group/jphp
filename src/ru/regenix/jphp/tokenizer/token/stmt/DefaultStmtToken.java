package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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
