package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

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
