package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class UnarMinusExprToken extends OperatorExprToken {
    public UnarMinusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_UNAR_MINUS);
    }
}
