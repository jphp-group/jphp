package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class ModExprToken extends OperatorExprToken {
    public ModExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MOD);
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
