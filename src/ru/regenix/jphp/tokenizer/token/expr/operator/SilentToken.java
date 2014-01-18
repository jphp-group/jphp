package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.common.Association;
import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

/**
 * Silent langMode token
 * @ callfunc()
 */
public class SilentToken extends OperatorExprToken {
    public SilentToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 21;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1;
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.RIGHT;
    }
}
