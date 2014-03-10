package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignXorExprToken extends AssignOperatorExprToken {
    public AssignXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_XOR_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitXor";
    }

    @Override
    public String getOperatorCode() {
        return "bitXor";
    }
}
