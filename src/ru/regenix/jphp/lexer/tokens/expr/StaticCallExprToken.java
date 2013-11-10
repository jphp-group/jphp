package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.CallExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.NameToken;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

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
