package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

import java.util.List;

public class CatchStmtToken extends StmtToken {
    private List<FulledNameToken> exceptions;
    private VariableExprToken variable;
    private BodyStmtToken body;

    public CatchStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CATCH);
    }

    public List<FulledNameToken> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<FulledNameToken> exceptions) {
        this.exceptions = exceptions;
    }

    /*public FulledNameToken getException() {
        return exception;
    }

    public void setException(FulledNameToken exception) {
        this.exception = exception;
    }*/

    public VariableExprToken getVariable() {
        return variable;
    }

    public void setVariable(VariableExprToken variable) {
        this.variable = variable;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
