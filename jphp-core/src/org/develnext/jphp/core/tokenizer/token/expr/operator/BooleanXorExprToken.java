package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class BooleanXorExprToken extends OperatorExprToken {
    public BooleanXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_XOR);
    }

    @Override
    public int getPriority() {
        return 180;
    }
}
