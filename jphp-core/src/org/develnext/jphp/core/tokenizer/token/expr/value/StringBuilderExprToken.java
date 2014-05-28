package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

import java.util.List;

public class StringBuilderExprToken extends ValueExprToken {
    protected List<Token> expression;
    protected boolean binary;

    public StringBuilderExprToken(TokenMeta meta, List<Token> expression) {
        super(meta, TokenType.T_CONSTANT_ENCAPSED_STRING);
        this.expression = expression;
    }

    public List<Token> getExpression() {
        return expression;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }
}
