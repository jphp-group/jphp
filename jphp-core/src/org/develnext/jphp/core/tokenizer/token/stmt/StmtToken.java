package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;

abstract public class StmtToken extends Token {
    public StmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
