package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.CallExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class DynamicCallExprToken extends CallExprToken {
    private ExprStmtToken object;
    private Token method;
    private ExprStmtToken methodExpr;

    public DynamicCallExprToken(TokenMeta meta) {
        super(meta);
    }

    public ExprStmtToken getObject() {
        return object;
    }

    public void setObject(ExprStmtToken object) {
        this.object = object;
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
}
