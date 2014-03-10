package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

public class IssetExprToken extends CallExprToken {
    public IssetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_ISSET);
    }
}
