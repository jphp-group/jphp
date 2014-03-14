package org.develnext.jphp.core.tokenizer.token;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.StmtToken;

public class OpenEchoTagToken extends StmtToken {

    private ExprStmtToken value;

    public OpenEchoTagToken(TokenMeta meta) {
        super(meta, TokenType.T_OPEN_TAG_WITH_ECHO);
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
