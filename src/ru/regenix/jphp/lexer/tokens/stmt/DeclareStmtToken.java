package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class DeclareStmtToken extends StmtToken {
    public DeclareStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_DECLARE);
    }
}
