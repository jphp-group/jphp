package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.StmtToken;

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
