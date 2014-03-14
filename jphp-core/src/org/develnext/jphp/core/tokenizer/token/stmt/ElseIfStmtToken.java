package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ElseIfStmtToken extends EndifStmtToken {
    public ElseIfStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ELSEIF);
    }
}
