package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

import java.util.List;

public class GlobalStmtToken extends StmtToken {

    // list of vars or var-vars
    private List<ValueExprToken> variables;

    public GlobalStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_GLOBAL);
    }

    public List<ValueExprToken> getVariables() {
        return variables;
    }

    public void setVariables(List<ValueExprToken> variables) {
        this.variables = variables;
    }
}
