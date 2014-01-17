package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.common.Association;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class ArrayPushExprToken extends OperatorExprToken {
    public ArrayPushExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_ARRAY_PUSH);
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.LEFT;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public String getCode() {
        return "refOfPush";
    }

    @Override
    public boolean isTraceNeeded() {
        return true;
    }
}
