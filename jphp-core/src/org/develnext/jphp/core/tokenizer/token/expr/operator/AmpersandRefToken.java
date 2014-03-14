package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class AmpersandRefToken extends OperatorExprToken {
    public AmpersandRefToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.RIGHT;
    }
}
