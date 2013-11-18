package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class ExtendsStmtToken extends StmtToken {

    private FulledNameToken name;

    public ExtendsStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_EXTENDS);
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }
}
