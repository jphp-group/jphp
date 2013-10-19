package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class ClassStmtToken extends StmtToken {
    private Modifier modifier;
    private boolean isFinal;
    private boolean isAbstract;

    private NamespaceStmtToken namespace;
    private NameToken name;
    private ExtendsStmtToken extend;
    private ImplementsStmtToken implement;

    private MethodStmtToken constructor;
    private List<ClassConstStmtToken> constants;
    private List<ClassVarStmtToken> properties;
    private List<MethodStmtToken> methods;

    public ClassStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CLASS);
    }

    public NameToken getName() {
        return name;
    }

    public void setName(NameToken name) {
        this.name = name;
    }

    public ExtendsStmtToken getExtend() {
        return extend;
    }

    public void setExtend(ExtendsStmtToken extend) {
        this.extend = extend;
    }

    public ImplementsStmtToken getImplement() {
        return implement;
    }

    public void setImplement(ImplementsStmtToken implement) {
        this.implement = implement;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public MethodStmtToken getConstructor() {
        return constructor;
    }

    public void setConstructor(MethodStmtToken constructor) {
        this.constructor = constructor;
    }

    public List<ClassConstStmtToken> getConstants() {
        return constants;
    }

    public void setConstants(List<ClassConstStmtToken> constants) {
        this.constants = constants;
    }

    public List<ClassVarStmtToken> getProperties() {
        return properties;
    }

    public void setProperties(List<ClassVarStmtToken> properties) {
        this.properties = properties;
    }

    public List<MethodStmtToken> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodStmtToken> methods) {
        this.methods = methods;
    }

    public NamespaceStmtToken getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceStmtToken namespace) {
        this.namespace = namespace;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }
}
