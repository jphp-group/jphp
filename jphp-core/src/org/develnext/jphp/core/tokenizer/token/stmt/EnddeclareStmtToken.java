package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class EnddeclareStmtToken extends EndStmtToken {
    public EnddeclareStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ENDDECLARE);
    }
}
