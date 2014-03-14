package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class DynamicAccessUnsetExprToken extends DynamicAccessExprToken {
    private ExprStmtToken value;

    public DynamicAccessUnsetExprToken(DynamicAccessExprToken token){
        super(token.getMeta());
        setField(token.getField());
        setFieldExpr(token.getFieldExpr());
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
