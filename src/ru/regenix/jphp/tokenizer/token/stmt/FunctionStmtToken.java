package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionStmtToken extends StmtToken {
    protected Modifier modifier;
    protected NamespaceStmtToken namespace;
    protected NameToken name;

    protected boolean returnReference;
    protected List<ArgumentStmtToken> arguments;
    protected BodyStmtToken body;
    protected boolean interfacable;

    protected Set<VariableExprToken> local;
    protected Set<VariableExprToken> passedLocal;
    protected Set<VariableExprToken> arrayAccessLocal;

    protected boolean dynamicLocal;
    protected boolean callsExist;
    protected boolean varsExist;

    public FunctionStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNCTION);
        this.dynamicLocal = false;
        this.callsExist = false;
        this.varsExist = false;
        this.passedLocal = new HashSet<VariableExprToken>();
        this.arrayAccessLocal = new HashSet<VariableExprToken>();
    }

    public NamespaceStmtToken getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceStmtToken namespace) {
        this.namespace = namespace;
    }

    public NameToken getName() {
        return name;
    }

    public void setName(NameToken name) {
        this.name = name;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public boolean isReturnReference() {
        return returnReference;
    }

    public void setReturnReference(boolean returnReference) {
        this.returnReference = returnReference;
    }

    public List<ArgumentStmtToken> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgumentStmtToken> arguments) {
        this.arguments = arguments;
    }

    public BodyStmtToken getBody() {
        return body;
    }

    public void setBody(BodyStmtToken body) {
        this.body = body;
    }

    public boolean isInterfacable() {
        return interfacable;
    }

    public void setInterfacable(boolean interfacable) {
        this.interfacable = interfacable;
    }

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }

    public Set<VariableExprToken> getPassedLocal() {
        return passedLocal;
    }

    public void setPassedLocal(Set<VariableExprToken> passedLocal) {
        this.passedLocal = passedLocal;
    }

    public boolean isDynamicLocal() {
        return dynamicLocal;
    }

    public void setDynamicLocal(boolean dynamicLocal) {
        this.dynamicLocal = dynamicLocal;
    }

    public boolean isCallsExist() {
        return callsExist;
    }

    public void setCallsExist(boolean callsExist) {
        this.callsExist = callsExist;
    }

    public boolean isReference(VariableExprToken variable){
        return dynamicLocal || arrayAccessLocal.contains(variable) || passedLocal.contains(variable);
    }

    public boolean isVarsExist() {
        return varsExist;
    }

    public void setVarsExist(boolean varsExist) {
        this.varsExist = varsExist;
    }

    public boolean isMutable(){
        return varsExist || callsExist;
    }

    public Set<VariableExprToken> getArrayAccessLocal() {
        return arrayAccessLocal;
    }

    public void setArrayAccessLocal(Set<VariableExprToken> arrayAccessLocal) {
        this.arrayAccessLocal = arrayAccessLocal;
    }
}
