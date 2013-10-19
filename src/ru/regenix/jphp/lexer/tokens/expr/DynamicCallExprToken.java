package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

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
