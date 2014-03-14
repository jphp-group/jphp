package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ContinueStmtToken extends JumpStmtToken {
    public ContinueStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CONTINUE);
    }
}
