package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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
