package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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
