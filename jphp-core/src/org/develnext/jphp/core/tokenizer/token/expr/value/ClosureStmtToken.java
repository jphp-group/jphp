package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;

public class ClosureStmtToken extends ValueExprToken {
    protected FunctionStmtToken function;
    protected ClassStmtToken ownerClass;
    protected int id;

    public ClosureStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public FunctionStmtToken getFunction() {
        return function;
    }

    public void setFunction(FunctionStmtToken function) {
        this.function = function;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClassStmtToken getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(ClassStmtToken ownerClass) {
        this.ownerClass = ownerClass;
    }
}
