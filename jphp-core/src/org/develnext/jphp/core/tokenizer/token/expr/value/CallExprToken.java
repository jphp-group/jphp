package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.List;

public class CallExprToken extends ValueExprToken implements CallableExprToken {
    private ExprToken name;
    private List<ExprStmtToken> parameters;

    protected CallExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public CallExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    public void setName(ExprToken name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }
}
