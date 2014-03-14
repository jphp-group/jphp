package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class BooleanAndExprToken extends LogicOperatorExprToken {
    public BooleanAndExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_AND);
    }

    @Override
    public int getPriority() {
        return 120;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.toBoolean() && o2.toBoolean() ? Memory.TRUE : Memory.FALSE;
    }
}
