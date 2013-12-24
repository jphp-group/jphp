package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class InterfaceStmtToken extends StmtToken {
    public InterfaceStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_INTERFACE);
    }
}
