package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class ReturnStmtToken extends StmtToken {

    private ExprStmtToken value;

    public ReturnStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_RETURN);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
