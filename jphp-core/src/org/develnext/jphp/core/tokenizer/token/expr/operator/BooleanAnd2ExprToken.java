package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

/**
 * and symbol
 */
public class BooleanAnd2ExprToken extends LogicOperatorExprToken {
    public BooleanAnd2ExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_AND);
    }

    @Override
    public int getPriority() {
        return 170;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.toBoolean() && o2.toBoolean() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
