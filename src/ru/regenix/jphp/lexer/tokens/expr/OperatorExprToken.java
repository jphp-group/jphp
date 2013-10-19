package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class OperatorExprToken extends ExprToken {
    private ExprToken left;
    private ExprToken right;

    public OperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public ExprToken getLeft() {
        return left;
    }

    public void setLeft(ExprToken left) {
        this.left = left;
    }

    public ExprToken getRight() {
        return right;
    }

    public void setRight(ExprToken right) {
        this.right = right;
    }
}
