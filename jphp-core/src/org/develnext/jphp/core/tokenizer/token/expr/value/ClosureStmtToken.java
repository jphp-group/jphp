package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;

public class ClosureStmtToken extends ValueExprToken {
    protected FunctionStmtToken function;
    protected FunctionStmtToken parentFunction;
    protected ClassStmtToken ownerClass;
    protected int id;
    protected boolean _static;

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

    public boolean isStatic() {
        return _static;
    }

    public void setStatic(boolean _static) {
        this._static = _static;
    }

    public ClassStmtToken getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(ClassStmtToken ownerClass) {
        this.ownerClass = ownerClass;
    }

    public FunctionStmtToken getParentFunction() {
        return parentFunction;
    }

    public void setParentFunction(FunctionStmtToken parentFunction) {
        this.parentFunction = parentFunction;
    }
}
