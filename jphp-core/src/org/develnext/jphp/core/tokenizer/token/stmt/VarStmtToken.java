package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class VarStmtToken extends StmtToken {
    public VarStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_VAR);
    }
}
