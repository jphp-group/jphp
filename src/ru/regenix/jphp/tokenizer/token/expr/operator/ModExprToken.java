package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

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
