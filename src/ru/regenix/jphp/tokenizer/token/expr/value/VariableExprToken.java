package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class VariableExprToken extends ValueExprToken {

    private String name;

    public VariableExprToken(TokenMeta meta) {
        super(meta, TokenType.T_VARIABLE);
        this.name = meta.getWord().substring(1); // skip $
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
