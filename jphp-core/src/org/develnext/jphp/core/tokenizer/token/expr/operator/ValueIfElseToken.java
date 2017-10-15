package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

public class ValueIfElseToken extends OperatorExprToken {
    public ExprStmtToken value;
    private ExprStmtToken alternative;

    public ValueIfElseToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 140;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    public ExprStmtToken getAlternative() {
        return alternative;
    }

    public void setAlternative(ExprStmtToken alternative) {
        this.alternative = alternative;
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.LEFT;
    }
}
