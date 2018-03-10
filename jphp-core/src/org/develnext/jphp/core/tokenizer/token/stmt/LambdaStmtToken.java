package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.ClosureStmtToken;

public class LambdaStmtToken extends StmtToken {
    private FunctionStmtToken function;
    private FunctionStmtToken parentFunction;

    public LambdaStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public FunctionStmtToken getFunction() {
        return function;
    }

    public void setFunction(FunctionStmtToken function) {
        this.function = function;
    }

    @Override
    public boolean isNamedToken() {
        return false;
    }

    public FunctionStmtToken getParentFunction() {
        return parentFunction;
    }

    public void setParentFunction(FunctionStmtToken parentFunction) {
        this.parentFunction = parentFunction;
    }
}
