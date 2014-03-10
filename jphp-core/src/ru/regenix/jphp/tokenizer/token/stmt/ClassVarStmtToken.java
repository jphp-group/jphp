package ru.regenix.jphp.tokenizer.token.stmt;

import php.runtime.common.Modifier;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

public class ClassVarStmtToken extends StmtToken {
    private boolean isStatic;
    private Modifier modifier;

    private VariableExprToken variable;
    private ExprStmtToken value;

    public ClassVarStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_VAR);
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public VariableExprToken getVariable() {
        return variable;
    }

    public void setVariable(VariableExprToken variable) {
        this.variable = variable;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}
