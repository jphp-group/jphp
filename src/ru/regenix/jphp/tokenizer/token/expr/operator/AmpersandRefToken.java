package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.common.Association;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

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
