package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.CallExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.CallableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

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
