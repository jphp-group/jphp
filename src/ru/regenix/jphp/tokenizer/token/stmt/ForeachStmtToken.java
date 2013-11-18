package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

public class ForeachStmtToken extends StmtToken {
    private ExprStmtToken list;
    private VariableExprToken key;
    private VariableExprToken value;

    private boolean valueReference;

    public ForeachStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FOREACH);
    }

    public ExprStmtToken getList() {
        return list;
    }

    public void setList(ExprStmtToken list) {
        this.list = list;
    }

    public VariableExprToken getKey() {
        return key;
    }

    public void setKey(VariableExprToken key) {
        this.key = key;
    }

    public VariableExprToken getValue() {
        return value;
    }

    public void setValue(VariableExprToken value) {
        this.value = value;
    }

    public boolean isValueReference() {
        return valueReference;
    }

    public void setValueReference(boolean valueReference) {
        this.valueReference = valueReference;
    }
}
