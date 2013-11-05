package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ArrayStmtToken extends StmtToken {
    public ArrayStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_ARRAY);
    }
}
