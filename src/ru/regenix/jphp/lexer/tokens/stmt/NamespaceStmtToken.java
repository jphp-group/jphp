package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.FulledNameToken;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStmtToken extends StmtToken {

    private FulledNameToken name;
    private final List<NamespaceUseStmtToken> uses;
    private BodyStmtToken body;

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

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    private static NamespaceStmtToken defaultNamespace =
            new NamespaceStmtToken(new TokenMeta("namespace", 0, 0, 0, 0))
            .setName(null);

    public static NamespaceStmtToken getDefault(){
        return defaultNamespace;
    }
}
