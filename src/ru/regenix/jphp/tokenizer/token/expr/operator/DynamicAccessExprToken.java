package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.common.Association;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

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
