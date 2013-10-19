package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

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
