package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class EndswitchStmtToken extends EndStmtToken {

    public EndswitchStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public EndswitchStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDSWITCH);
    }
}
