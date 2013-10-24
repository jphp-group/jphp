package ru.regenix.jphp.lexer.tokens.expr.value;


import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

public class GetVarExprToken extends ValueExprToken {
    private ExprStmtToken name;

    public GetVarExprToken(TokenMeta meta) {
        super(meta, TokenType.T_VARIABLE);
    }

    public ExprStmtToken getName() {
        return name;
    }

    public void setName(ExprStmtToken name) {
        this.name = name;
    }
}
