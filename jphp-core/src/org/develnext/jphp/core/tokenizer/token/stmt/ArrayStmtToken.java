package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ArrayStmtToken extends StmtToken {
    public ArrayStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ARRAY);
    }
}
