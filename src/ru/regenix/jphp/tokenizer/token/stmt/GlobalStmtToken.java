package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

import java.util.List;

public class GlobalStmtToken extends StmtToken {

    private List<VariableExprToken> variable;

    public GlobalStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_GLOBAL);
    }

    public List<VariableExprToken> getVariable() {
        return variable;
    }

    public void setVariable(List<VariableExprToken> variable) {
        this.variable = variable;
    }
}
