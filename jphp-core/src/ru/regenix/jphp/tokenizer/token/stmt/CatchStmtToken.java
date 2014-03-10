package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

public class CatchStmtToken extends StmtToken {
    private FulledNameToken exception;
    private VariableExprToken variable;
    private BodyStmtToken body;

    public CatchStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CATCH);
    }

    public FulledNameToken getException() {
        return exception;
    }

    public void setException(FulledNameToken exception) {
        this.exception = exception;
    }

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
}
