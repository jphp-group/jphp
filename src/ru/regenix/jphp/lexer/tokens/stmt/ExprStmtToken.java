package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class ExprStmtToken extends StmtToken {
    private List<Token> tokens;

    protected ExprStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public ExprStmtToken(List<Token> tokens){
        this(TokenMeta.of(tokens));
        setTokens(tokens);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
