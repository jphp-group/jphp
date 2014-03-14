package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

abstract public class AssignOperatorExprToken extends OperatorExprToken
    implements AssignableOperatorToken {
    public AssignOperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    @Override
    public int getPriority() {
        return 150;
    }

    @Override
    public boolean isRightSide() {
        return true;
    }

    abstract public String getOperatorCode();
}
