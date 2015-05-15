package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStmtToken extends StmtToken {

    private FulledNameToken name;
    private final List<NamespaceUseStmtToken> uses;
    private List<Token> tree;
    private boolean tokenRegistered;

    public NamespaceStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_NAMESPACE);
        this.uses = new ArrayList<NamespaceUseStmtToken>();
    }

    public List<NamespaceUseStmtToken> getUses() {
        return uses;
    }

    public FulledNameToken getName() {
        return name;
    }

    public NamespaceStmtToken setName(FulledNameToken name) {
        this.name = name;
        return this;
    }

    public List<Token> getTree() {
        return tree;
    }

    public void setTree(List<Token> tree) {
        this.tree = tree;
    }

    public static NamespaceStmtToken getDefault(){
        return new NamespaceStmtToken(TokenMeta.empty())
                .setName(null);
    }

    public boolean isTokenRegistered() {
        return tokenRegistered;
    }

    public void setTokenRegistered(boolean tokenRegistered) {
        this.tokenRegistered = tokenRegistered;
    }
}
