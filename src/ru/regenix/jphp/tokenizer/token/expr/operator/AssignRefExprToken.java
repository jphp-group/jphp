package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class AssignRefExprToken extends OperatorExprToken {
    public AssignRefExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_EQUAL);
    }

    @Override
    public int getPriority() {
        return 150;
    }
}
