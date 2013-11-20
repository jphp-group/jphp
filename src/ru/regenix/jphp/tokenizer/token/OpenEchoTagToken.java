package ru.regenix.jphp.tokenizer.token;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.StmtToken;

public class OpenEchoTagToken extends StmtToken {

    private ExprStmtToken value;

    public OpenEchoTagToken(TokenMeta meta) {
        super(meta, TokenType.T_OPEN_TAG_WITH_ECHO);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
