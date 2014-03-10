package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStmtToken extends StmtToken {

    private FulledNameToken name;
    private final List<NamespaceUseStmtToken> uses;
    private List<Token> tree;

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
}
