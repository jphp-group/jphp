package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class AssignConcatExprToken extends AssignOperatorExprToken {
    public AssignConcatExprToken(TokenMeta meta) {
        super(meta, TokenType.T_CONCAT_EQUAL);
    }

    @Override
    public String getCode() {
        return "assignConcat";
    }

    @Override
    public String getOperatorCode() {
        return "concat";
    }
}
