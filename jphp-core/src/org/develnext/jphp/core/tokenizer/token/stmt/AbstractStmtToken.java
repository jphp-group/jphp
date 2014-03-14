package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class AbstractStmtToken extends StmtToken {
    public AbstractStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ABSTRACT);
    }
}
