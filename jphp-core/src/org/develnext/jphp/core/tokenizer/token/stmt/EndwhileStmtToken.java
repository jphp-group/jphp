package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class EndwhileStmtToken extends EndStmtToken {
    public EndwhileStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDWHILE);
    }
}
