package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;

public class ArgumentStmtToken extends StmtToken {
    private boolean reference;
    private VariableExprToken name;
    private ExprToken value;

    public ArgumentStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public VariableExprToken getName() {
        return name;
    }

    public void setName(VariableExprToken name) {
        this.name = name;
    }

    public ExprToken getValue() {
        return value;
    }

    public void setValue(ExprToken value) {
        this.value = value;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }
}
