package org.develnext.jphp.core.tokenizer.token.stmt;


import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class InsteadofStmtToken extends StmtToken {
    public InsteadofStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_INSTEADOF);
    }
}
