package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class EndforeachStmtToken extends EndStmtToken {
    public EndforeachStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDFOREACH);
    }
}
