package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class DeclareStmtToken extends StmtToken {
    public DeclareStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DECLARE);
    }
}
