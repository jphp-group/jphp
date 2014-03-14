package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;

abstract public class ExprToken extends Token {
    public ExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public int getPriority(){
        return 0;
    }

    public Token getLast(){
        return this;
    }
}
