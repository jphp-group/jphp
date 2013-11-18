package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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
