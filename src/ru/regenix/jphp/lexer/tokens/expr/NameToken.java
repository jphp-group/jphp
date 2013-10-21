package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class NameToken extends ExprToken {

    private String name;

    public NameToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
        this.name = meta.getWord();
    }

    public String getName() {
        return name;
    }
}
