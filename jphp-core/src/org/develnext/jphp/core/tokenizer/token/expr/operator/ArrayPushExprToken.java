package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

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
