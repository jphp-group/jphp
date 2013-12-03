package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

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
