package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class NotExprToken extends OperatorExprToken {
    public NotExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.RIGHT;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 21;
    }

    @Override
    public String getCode() {
        return "bitNot";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.bitNot();
    }
}
