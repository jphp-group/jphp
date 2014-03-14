package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class EndforStmtToken extends EndStmtToken {
    public EndforStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDFOR);
    }
}
