package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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
