package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class DynamicAccessExprToken extends OperatorExprToken {
    protected ValueExprToken field;
    protected ExprStmtToken fieldExpr;

    public DynamicAccessExprToken(TokenMeta meta) {
        super(meta, TokenType.T_OBJECT_OPERATOR);
    }

    public ValueExprToken getField() {
        return field;
    }

    public void setField(ValueExprToken field) {
        this.field = field;
    }

    public ExprStmtToken getFieldExpr() {
        return fieldExpr;
    }

    public void setFieldExpr(ExprStmtToken fieldExpr) {
        this.fieldExpr = fieldExpr;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.LEFT;
    }
}
