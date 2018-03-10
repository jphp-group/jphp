package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class LambdaStmtToken extends StmtToken {
    private FunctionStmtToken function;

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
}
