package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class BreakStmtToken extends JumpStmtToken {
    public BreakStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_BREAK);
    }
}
