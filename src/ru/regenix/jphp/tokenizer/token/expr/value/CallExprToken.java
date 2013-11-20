package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

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

    public Token getName() {
        return name;
    }
}
