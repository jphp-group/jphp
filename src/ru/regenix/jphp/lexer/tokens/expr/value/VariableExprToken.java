package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class VariableExprToken extends ValueExprToken {

    private String name;

    public VariableExprToken(TokenMeta meta) {
        super(meta, TokenType.T_VARIABLE);
        this.name = meta.getWord().substring(1); // skip $
    }

    public String getName() {
        return name;
    }
}
