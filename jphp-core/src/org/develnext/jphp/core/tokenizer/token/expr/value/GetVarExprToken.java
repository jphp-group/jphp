package org.develnext.jphp.core.tokenizer.token.expr.value;


import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class GetVarExprToken extends ValueExprToken implements VariableValueExprToken {
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
