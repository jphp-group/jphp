package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

import java.util.List;

public class GlobalStmtToken extends StmtToken {

    private List<VariableExprToken> variables;

    public GlobalStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_GLOBAL);
    }

    public List<VariableExprToken> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableExprToken> variables) {
        this.variables = variables;
    }
}
