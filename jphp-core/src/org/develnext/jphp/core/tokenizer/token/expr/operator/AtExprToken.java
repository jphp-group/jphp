package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class AtExprToken extends OperatorExprToken {
    public AtExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
