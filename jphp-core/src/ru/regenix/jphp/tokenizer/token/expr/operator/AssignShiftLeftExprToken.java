package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignShiftLeftExprToken extends AssignOperatorExprToken {
    public AssignShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignBitShl";
    }

    @Override
    public String getOperatorCode() {
        return "bitShl";
    }
}
