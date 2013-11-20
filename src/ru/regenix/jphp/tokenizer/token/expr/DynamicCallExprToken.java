package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.CallExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

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
