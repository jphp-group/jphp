package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class EchoRawToken extends StmtToken {
    public EchoRawToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
