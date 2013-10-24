package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class ModExprToken extends OperatorExprToken {
    public ModExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MOD);
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
