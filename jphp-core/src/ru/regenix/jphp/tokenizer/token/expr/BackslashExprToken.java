package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class BackslashExprToken extends ExprToken {
    public BackslashExprToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_SEPARATOR);
    }
}
