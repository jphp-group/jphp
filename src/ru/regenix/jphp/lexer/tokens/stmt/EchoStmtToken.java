package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

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
