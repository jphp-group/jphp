package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class CommaToken extends ExprToken {
    public CommaToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
