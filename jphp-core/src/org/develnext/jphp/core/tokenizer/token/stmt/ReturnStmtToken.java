package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ReturnStmtToken extends StmtToken {

    private ExprStmtToken value;

    public ReturnStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_RETURN);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
