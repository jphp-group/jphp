package ru.regenix.jphp.lexer.tokens.stmt;


import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AsStmtToken extends StmtToken {
    public AsStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_AS);
    }
}
