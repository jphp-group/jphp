package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class TraitStmtToken extends StmtToken {
    public TraitStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_TRAIT);
    }
}
