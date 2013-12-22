package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.CallExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.CallableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.util.List;

public class CallOperatorToken extends OperatorExprToken implements CallableExprToken {
    private List<ExprStmtToken> parameters;

    protected CallOperatorToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public CallOperatorToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public CallOperatorToken(CallExprToken call) {
        this(call.getMeta());
        parameters = call.getParameters();
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 15;
    }
}
