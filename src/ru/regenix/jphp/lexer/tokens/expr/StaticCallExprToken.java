package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

public class StaticCallExprToken extends CallExprToken {
    private Token name;
    private Token method;
    private ExprStmtToken methodExpr;

    public StaticCallExprToken(TokenMeta meta) {
        super(meta);
    }

    public Token getName() {
        return name;
    }

    public void setName(NameToken name) {
        this.name = name;
    }

    public void setName(VariableExprToken name){
        this.name = name;
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

