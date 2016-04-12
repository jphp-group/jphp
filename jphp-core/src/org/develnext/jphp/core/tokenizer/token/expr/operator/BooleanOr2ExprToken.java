package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

/**
 * or symbol
 */
public class BooleanOr2ExprToken extends LogicOperatorExprToken {
    public BooleanOr2ExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_OR);
    }

    @Override
    public int getPriority() {
        return 190;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.toBoolean() || o2.toBoolean() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
