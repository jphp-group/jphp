package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.util.List;

public class ArrayGetExprToken extends OperatorExprToken {
    private List<ExprStmtToken> parameters;

    public ArrayGetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_ARRAY_ACCESS);
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
