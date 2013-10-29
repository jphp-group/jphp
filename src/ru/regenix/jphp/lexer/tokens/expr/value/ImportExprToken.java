package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

abstract public class ImportExprToken extends ValueExprToken {
    private ExprStmtToken value;

    public ImportExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
