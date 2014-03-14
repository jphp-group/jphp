package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ProtectedStmtToken extends StmtToken {
    public ProtectedStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_PROTECTED);
    }
}
