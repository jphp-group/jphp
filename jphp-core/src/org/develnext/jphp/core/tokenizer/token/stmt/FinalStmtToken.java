package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class FinalStmtToken extends StmtToken {
    public FinalStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FINAL);
    }
}
