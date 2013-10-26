package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

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
