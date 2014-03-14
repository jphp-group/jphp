package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class UsesStmtToken extends StmtToken {
    public UsesStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
    }
}
