package org.develnext.jphp.core.tokenizer.token.stmt;


import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AsStmtToken extends StmtToken {
    public AsStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_AS);
    }
}
