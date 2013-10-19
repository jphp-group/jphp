package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class NamespaceStmtToken extends StmtToken {

    private FulledNameToken name;
    private List<NamespaceUseStmtToken> uses;

    public NamespaceStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_NAMESPACE);
    }

    public List<NamespaceUseStmtToken> getUses() {
        return uses;
    }

    public void setUses(List<NamespaceUseStmtToken> uses) {
        this.uses = uses;
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }
}
