package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class ShiftRightExprToken extends OperatorExprToken {
    public ShiftRightExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SR);
    }
}
