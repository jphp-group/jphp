package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.util.List;

public class ArrayGetExprToken extends ExprToken {
    private List<ExprStmtToken> parameters;

    public ArrayGetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_ARRAY_ACCESS);
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }
}
