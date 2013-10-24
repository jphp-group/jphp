package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class PlusExprToken extends OperatorExprToken {
    public PlusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_PLUS);
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
