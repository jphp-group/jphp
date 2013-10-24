package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class MinusExprToken extends OperatorExprToken {
    public MinusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MINUS);
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
