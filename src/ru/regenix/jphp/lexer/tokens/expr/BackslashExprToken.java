package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class BackslashExprToken extends ExprToken {
    public BackslashExprToken(TokenMeta meta) {
        super(meta, TokenType.T_NS_SEPARATOR);
    }
}
