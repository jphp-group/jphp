package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class RequireStmtToken extends StmtToken {
    private ExprStmtToken value;

    public RequireStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
