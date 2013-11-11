package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.util.List;

public class CallExprToken extends ValueExprToken {
    private ValueExprToken name;
    private List<ExprStmtToken> parameters;

    public CallExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    public void setName(ValueExprToken name) {
        this.name = name;
    }

    public ValueExprToken getName() {
        return name;
    }
}
