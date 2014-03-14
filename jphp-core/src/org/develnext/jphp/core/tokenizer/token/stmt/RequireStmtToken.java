package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class RequireStmtToken extends StmtToken {
    private ExprStmtToken value;

    public RequireStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
