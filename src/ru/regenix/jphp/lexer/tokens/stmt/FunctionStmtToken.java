package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;

import java.util.List;

public class FunctionStmtToken extends StmtToken {
    protected Modifier modifier;
    protected NamespaceStmtToken namespace;
    protected NameToken name;

    protected boolean returnReference;
    protected List<ArgumentStmtToken> arguments;
    protected BodyStmtToken body;
    protected boolean interfacable;

    protected List<VariableExprToken> local;

    protected boolean dynamicLocal;
    protected boolean callsExist;
    protected boolean varsExist;

    public FunctionStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNCTION);
        this.dynamicLocal = false;
        this.callsExist = false;
        this.varsExist = false;
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

    public List<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(List<VariableExprToken> local) {
        this.local = local;
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

    public boolean isVarsExist() {
        return varsExist;
    }

    public void setVarsExist(boolean varsExist) {
        this.varsExist = varsExist;
    }

    public boolean isMutable(){
        return varsExist || callsExist;
    }
}
