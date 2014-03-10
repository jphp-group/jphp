package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

public class DynamicAccessAssignExprToken extends DynamicAccessExprToken {
    private Token assignOperator;
    private ExprStmtToken value;

    public DynamicAccessAssignExprToken(DynamicAccessExprToken token){
        super(token.getMeta());
        setField(token.getField());
        setFieldExpr(token.getFieldExpr());
    }

    public Token getAssignOperator() {
        return assignOperator;
    }

    public void setAssignOperator(Token assignOperator) {
        this.assignOperator = assignOperator;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
