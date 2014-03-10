package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

import java.util.Set;

public class ForeachStmtToken extends StmtToken {
    private Set<VariableExprToken> local;
    private ExprStmtToken iterator;
    private BodyStmtToken body;
    private VariableExprToken key;
    private ExprStmtToken value;

    private boolean valueReference;
    private boolean keyReference;

    public ForeachStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FOREACH);
    }

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    public VariableExprToken getKey() {
        return key;
    }

    public void setKey(VariableExprToken key) {
        this.key = key;
    }

    public boolean isValueReference() {
        return valueReference;
    }

    public void setValueReference(boolean valueReference) {
        this.valueReference = valueReference;
    }

    public boolean isKeyReference() {
        return keyReference;
    }

    public void setKeyReference(boolean keyReference) {
        this.keyReference = keyReference;
    }

    public ExprStmtToken getIterator() {
        return iterator;
    }

    public void setIterator(ExprStmtToken iterator) {
        this.iterator = iterator;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
