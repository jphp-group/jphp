package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
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
    protected List<ArgumentStmtToken> uses;
    protected BodyStmtToken body;
    protected boolean interfacable;

    protected Set<VariableExprToken> local;
    protected Set<VariableExprToken> passedLocal;
    protected Set<VariableExprToken> arrayAccessLocal;
    protected Set<VariableExprToken> refLocal;
    protected Set<VariableExprToken> unstableLocal;

    protected boolean dynamicLocal;
    protected boolean callsExist;
    protected boolean varsExists;
    protected boolean thisExists;
    public static final VariableExprToken thisVariable = VariableExprToken.valueOf("this");

    public FunctionStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNCTION);
        this.dynamicLocal = false;
        this.callsExist = false;
        this.varsExists = false;
        this.thisExists = false;
        this.passedLocal = new HashSet<VariableExprToken>();
        this.arrayAccessLocal = new HashSet<VariableExprToken>();
        this.refLocal = new HashSet<VariableExprToken>();
        this.unstableLocal = new HashSet<VariableExprToken>();
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

    public List<ArgumentStmtToken> getUses() {
        return uses;
    }

    public void setUses(List<ArgumentStmtToken> uses) {
        this.uses = uses;
    }

    public Set<VariableExprToken> getRefLocal() {
        return refLocal;
    }

    public void setRefLocal(Set<VariableExprToken> refLocal) {
        this.refLocal = refLocal;
    }

    public Set<VariableExprToken> getUnstableLocal() {
        return unstableLocal;
    }

    public void setUnstableLocal(Set<VariableExprToken> unstableLocal) {
        this.unstableLocal = unstableLocal;
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
        this.thisExists = local.contains(thisVariable);
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
        return dynamicLocal || arrayAccessLocal.contains(variable) || passedLocal.contains(variable)
                || refLocal.contains(variable);
    }

    public boolean isUnstableVariable(VariableExprToken variable){
        return unstableLocal.contains(variable);
    }

    public boolean isVarsExists() {
        return varsExists;
    }

    public void setVarsExists(boolean varsExists) {
        this.varsExists = varsExists;
    }

    public boolean isMutable(){
        return varsExists || callsExist;
    }

    public Set<VariableExprToken> getArrayAccessLocal() {
        return arrayAccessLocal;
    }

    public void setArrayAccessLocal(Set<VariableExprToken> arrayAccessLocal) {
        this.arrayAccessLocal = arrayAccessLocal;
    }

    public String getFulledName(char delimiter){
        return namespace == null || namespace.getName() == null
                ? name.getName()
                : namespace.getName().toName(delimiter) + delimiter + name.getName();
    }

    public String getFulledName(){
        return getFulledName('\\');
    }

    public void setThisExists(boolean thisExists) {
        this.thisExists = thisExists;
    }

    public boolean isThisExists() {
        return thisExists;
    }
}
