package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class SmallerOrEqualToken extends OperatorExprToken {
    public SmallerOrEqualToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_SMALLER_OR_EQUAL);
    }

    @Override
    public int getPriority() {
        return 70;
    }
}
