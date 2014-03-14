package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class ModExprToken extends OperatorExprToken {
    public ModExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MOD);
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getCode() {
        return "mod";
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.mod(o2);
    }
}
