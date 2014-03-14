package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class AssignRefExprToken extends OperatorExprToken {
    public AssignRefExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_EQUAL);
    }

    @Override
    public int getPriority() {
        return 150;
    }
}
