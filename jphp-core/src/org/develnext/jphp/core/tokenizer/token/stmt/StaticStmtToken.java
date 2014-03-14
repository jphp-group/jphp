package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

public class StaticStmtToken extends StmtToken {
    protected VariableExprToken variable;
    protected ExprStmtToken initValue;

    public StaticStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_STATIC);
    }

    public VariableExprToken getVariable() {
        return variable;
    }

    public void setVariable(VariableExprToken variable) {
        this.variable = variable;
    }

    public ExprStmtToken getInitValue() {
        return initValue;
    }

    public void setInitValue(ExprStmtToken initValue) {
        this.initValue = initValue;
    }
}
