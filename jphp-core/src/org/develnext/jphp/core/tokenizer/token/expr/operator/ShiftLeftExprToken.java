package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

/**
 * <<
 */
public class ShiftLeftExprToken extends OperatorExprToken {
    public ShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL);
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public String getCode() {
        return "bitShl";
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.bitShl(o2);
    }
}
