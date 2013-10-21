package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.expr.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;

import java.util.List;

public class ImplementsStmtToken extends ExprToken {

    private List<FulledNameToken> names;

    public ImplementsStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_IMPLEMENTS);
    }

    public List<FulledNameToken> getNames() {
        return names;
    }

    public void setNames(List<FulledNameToken> names) {
        this.names = names;
    }
}
