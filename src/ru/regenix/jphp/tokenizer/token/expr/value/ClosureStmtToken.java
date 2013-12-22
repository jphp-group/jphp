package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.FunctionStmtToken;

public class ClosureStmtToken extends ValueExprToken {
    protected FunctionStmtToken function;
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
}
