package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.CallExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class StaticCallExprToken extends CallExprToken {
    private Token clazz;
    private Token method;
    private ExprStmtToken methodExpr;

    public StaticCallExprToken(TokenMeta meta) {
        super(meta);
    }

    public Token getMethod() {
        return method;
    }

    public void setMethod(NameToken method) {
        this.method = method;
    }

    public void setMethod(VariableExprToken method){
        this.method = method;
    }

    public ExprStmtToken getMethodExpr() {
        return methodExpr;
    }

    public void setMethodExpr(ExprStmtToken methodExpr) {
        this.methodExpr = methodExpr;
    }

    public Token getClazz() {
        return clazz;
    }

    public void setClazz(NameToken clazz) {
        this.clazz = clazz;
    }

    public void setClazz(VariableExprToken clazz) {
        this.clazz = clazz;
    }
}
