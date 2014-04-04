package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ExprToken;

import java.util.Iterator;
import java.util.List;

public class ImplementsStmtToken extends ExprToken implements Iterable<FulledNameToken> {

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

    @Override
    public Iterator<FulledNameToken> iterator() {
        return names.iterator();
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
