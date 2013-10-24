package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class SmallerExprToken extends OperatorExprToken {
    public SmallerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_SMALLER);
    }


    @Override
    public int getPriority() {
        return 70;
    }
}
