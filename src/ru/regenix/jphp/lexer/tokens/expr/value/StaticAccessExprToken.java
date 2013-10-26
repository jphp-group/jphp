package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class StaticAccessExprToken extends ValueExprToken {
    private Token clazz;
    private Token method;

    public StaticAccessExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DOUBLE_COLON);
    }

    public Token getClazz() {
        return clazz;
    }

    public void setClazz(Token clazz) {
        this.clazz = clazz;
    }

    public Token getMethod() {
        return method;
    }

    public void setMethod(Token method) {
        this.method = method;
    }
}
