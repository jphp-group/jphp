package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ExprToken;

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
