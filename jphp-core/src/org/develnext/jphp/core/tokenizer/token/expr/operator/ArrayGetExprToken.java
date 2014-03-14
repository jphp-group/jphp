package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.List;

public class ArrayGetExprToken extends OperatorExprToken {
    private List<ExprStmtToken> parameters;

    public ArrayGetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_ARRAY_ACCESS);
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.LEFT;
    }
}
