package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class PrivateStmtToken extends StmtToken {
    public PrivateStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PRIVATE);
    }
}
