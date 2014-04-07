package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.syntax.VariableStats;
import php.runtime.common.Modifier;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

import java.util.*;

public class FunctionStmtToken extends StmtToken {
    protected Modifier modifier;
    protected NamespaceStmtToken namespace;
    protected NameToken name;

    protected boolean returnReference;
    protected List<ArgumentStmtToken> arguments;
    protected List<ArgumentStmtToken> uses;
    protected BodyStmtToken body;
    protected boolean interfacable;

    protected Map<String, LabelStmtToken> labels;
    protected Map<String, VariableStats> variables;

    protected Set<VariableExprToken> local;
    protected Set<VariableExprToken> staticLocal;

    protected boolean dynamicLocal;
    protected boolean callsExist;
    protected boolean varsExists;
    protected boolean thisExists;
    public static final VariableExprToken thisVariable = VariableExprToken.valueOf("this");
    protected int id;

    protected boolean isStatic = false;

    public FunctionStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNCTION);
        this.dynamicLocal = false;
        this.callsExist = false;
        this.varsExists = false;
        this.thisExists = false;

        this.staticLocal = new HashSet<VariableExprToken>();
        this.variables = new LinkedHashMap<String, VariableStats>();
    }

    public Set<VariableExprToken> getStaticLocal() {
        return staticLocal;
    }

    public void setStaticLocal(Set<VariableExprToken> staticLocal) {
        this.staticLocal = staticLocal;
    }

    public static VariableExprToken getThisVariable() {
        return thisVariable;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
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

    public void setLabels(Map<String, LabelStmtToken> labels) {
        this.labels = labels;
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
        VariableStats stats = variable(variable);

        return dynamicLocal || stats.isArrayAccess() || stats.isPassed() || stats.isReference();
    }

    public boolean isUnstableVariable(VariableExprToken variable){
        return variable(variable).isUnstable();
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean addLabel(LabelStmtToken labelStmtToken) {
        if (labels == null)
            labels = new LinkedHashMap<String, LabelStmtToken>();

        return labels.put(labelStmtToken.getName().toLowerCase(), labelStmtToken) == null;
    }

    public LabelStmtToken findLabel(String name) {
        if (labels == null)
            return null;

        return labels.get(name.toLowerCase());
    }

    public VariableStats variable(String name) {
        VariableStats stats = variables.get(name);
        if (stats == null)
            variables.put(name, stats = new VariableStats());

        return stats;
    }

    public VariableStats variable(VariableExprToken token) {
        return variable(token.getName());
    }

    public boolean isUnusedVariable(VariableExprToken token) {
        return !dynamicLocal && variable(token).isUnused();
    }
}
