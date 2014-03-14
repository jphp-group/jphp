package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;

import java.util.List;

public class EchoStmtToken extends StmtToken {

    private List<ExprStmtToken> arguments;

    public EchoStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ECHO);
    }

    public List<ExprStmtToken> getArguments() {
        return arguments;
    }

    public void setArguments(List<ExprStmtToken> arguments) {
        this.arguments = arguments;
    }
}
