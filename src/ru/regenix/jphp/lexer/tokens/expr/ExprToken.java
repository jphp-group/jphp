package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

/**
 * User: Dim-S (dz@dim-s.net)
 * Date: 19.10.13
 */
abstract public class ExprToken extends Token {

    protected ExprToken(TokenMeta meta) {
        super(meta);
    }
}
