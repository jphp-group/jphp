package org.develnext.jphp.core.tokenizer.token.expr.value;

public class StaticAccessUnsetExprToken extends StaticAccessExprToken implements CallableExprToken {
    public StaticAccessUnsetExprToken(StaticAccessExprToken token) {
        super(token.getMeta());
        this.setField(token.field);
        this.setClazz(token.clazz);
        this.setFieldExpr(token.fieldExpr);
    }
}
