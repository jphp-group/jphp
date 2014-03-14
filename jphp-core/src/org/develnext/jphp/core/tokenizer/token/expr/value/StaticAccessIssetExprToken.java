package org.develnext.jphp.core.tokenizer.token.expr.value;

public class StaticAccessIssetExprToken extends StaticAccessExprToken {
    public StaticAccessIssetExprToken(StaticAccessExprToken token) {
        super(token.getMeta());
        this.setField(token.field);
        this.setClazz(token.clazz);
        this.setFieldExpr(token.fieldExpr);
    }
}
