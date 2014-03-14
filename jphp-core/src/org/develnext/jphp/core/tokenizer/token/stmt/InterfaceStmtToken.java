package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class InterfaceStmtToken extends StmtToken {
    public InterfaceStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_INTERFACE);
    }
}
