package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class NamespaceUseStmtToken extends StmtToken {

    private FulledNameToken name;
    private NameToken as;

    public NamespaceUseStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_USE);
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }

    public NameToken getAs() {
        return as;
    }

    public void setAs(NameToken as) {
        this.as = as;
    }
}
