package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class VariableExprToken extends ExprToken {

    private String name;

    protected VariableExprToken(TokenMeta meta) {
        super(meta);
        this.name = meta.getWord().substring(1); // skip $
    }

    public String getName() {
        return name;
    }
}
