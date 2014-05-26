package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

public class VariableExprToken extends ValueExprToken implements VariableValueExprToken {

    private String name;

    protected VariableExprToken(TokenMeta meta, String name){
        super(meta, TokenType.T_VARIABLE);
        this.name = name;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableExprToken)) return false;

        VariableExprToken token = (VariableExprToken) o;
        return token.getClass() == this.getClass() && name.equals(token.name);

    }

    public static VariableExprToken valueOf(String name){
        return new VariableExprToken(TokenMeta.empty(), name);
    }
}
