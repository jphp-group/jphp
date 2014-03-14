package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

import java.util.List;
import java.util.Set;

public class TryStmtToken extends StmtToken {
    private BodyStmtToken body;
    private Set<VariableExprToken> local;
    private List<CatchStmtToken> catches;
    private BodyStmtToken _finally;

    public TryStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_TRY);
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }

    public List<CatchStmtToken> getCatches() {
        return catches;
    }

    public void setCatches(List<CatchStmtToken> catches) {
        this.catches = catches;
    }

    public BodyStmtToken getFinally() {
        return _finally;
    }

    public void setFinally(BodyStmtToken _finally) {
        this._finally = _finally;
    }
}
