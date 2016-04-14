package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StaticAccessExprToken;
import php.runtime.common.Association;

public class StaticAccessOperatorExprToken extends OperatorExprToken {
    protected StaticAccessExprToken origin;

    public StaticAccessOperatorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DOUBLE_COLON);
    }

    public StaticAccessOperatorExprToken(StaticAccessExprToken token) {
        this(token.getMeta());
        origin = token;
    }

    public StaticAccessExprToken getOrigin() {
        return origin;
    }

    public void setOrigin(StaticAccessExprToken origin) {
        this.origin = origin;
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
    public Association getOnlyAssociation() {
        return Association.LEFT;
    }
}
