package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.TokenMeta;

import java.util.Arrays;
import java.util.List;

public class ExprStmtToken extends StmtToken {
    private List<Token> tokens;
    private boolean isStmtList = true;

    protected ExprStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public ExprStmtToken(List<Token> tokens){
        this(TokenMeta.of(tokens));
        setTokens(tokens);
    }

    public ExprStmtToken(Token... tokens){
        this(Arrays.asList(tokens));
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        for (Token el : tokens)
            if (!(el instanceof StmtToken)) {
                isStmtList = false;
                break;
            }

        this.tokens = tokens;
    }

    public boolean isSingle(){
        return tokens.size() == 1;
    }

    public Token getSingle(){
        return tokens.get(0);
    }

    public Token getLast(){
        if (tokens.size() == 0)
            return null;
        return tokens.get(tokens.size() - 1);
    }

    public boolean isStmtList() {
        return isStmtList;
    }
}
