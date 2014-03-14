package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class FinallyStmtToken extends StmtToken {
    public FinallyStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_FINALLY);
    }
}
