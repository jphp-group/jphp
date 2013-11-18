package ru.regenix.jphp.tokenizer.token.expr.value;


import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

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
