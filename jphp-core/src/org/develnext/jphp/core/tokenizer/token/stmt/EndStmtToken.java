package org.develnext.jphp.core.tokenizer.token.stmt;


import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

abstract public class EndStmtToken extends StmtToken {
    public EndStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
