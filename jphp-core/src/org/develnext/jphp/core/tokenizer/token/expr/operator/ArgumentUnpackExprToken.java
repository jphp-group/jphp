package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.common.Association;

public class ArgumentUnpackExprToken extends OperatorExprToken {
    public ArgumentUnpackExprToken(TokenMeta meta) {
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
    public boolean isSide() {
        return false;
    }

    @Override
    public String getCode() {
        return "unpack";
    }

    @Override
    public int getPriority() {
        return 200;
    }
}
