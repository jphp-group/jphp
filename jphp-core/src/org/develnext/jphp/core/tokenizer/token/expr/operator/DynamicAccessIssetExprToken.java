package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class DynamicAccessIssetExprToken extends DynamicAccessExprToken {
    private ExprStmtToken value;
    private boolean withMagic = true;

    public DynamicAccessIssetExprToken(DynamicAccessExprToken token){
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

    public boolean isWithMagic() {
        return withMagic;
    }

    public void setWithMagic(boolean withMagic) {
        this.withMagic = withMagic;
    }
}
