package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class EndifStmtToken extends EndStmtToken {

    public EndifStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public EndifStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDIF);
    }
}
