package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class InstanceofExprToken extends OperatorExprToken {

    public InstanceofExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INSTANCEOF);
    }

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
