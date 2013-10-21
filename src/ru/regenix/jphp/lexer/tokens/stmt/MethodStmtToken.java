package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class MethodStmtToken extends FunctionStmtToken {
    private boolean isAbstract;
    private boolean isFinal;
    private boolean isStatic;

    private ClassStmtToken clazz;

    public MethodStmtToken(TokenMeta meta) {
        super(meta);
    }

    public MethodStmtToken(FunctionStmtToken token){
        this(token.getMeta());
        setArguments(token.arguments);
        setBody(token.body);
        setModifier(token.modifier);
        setName(token.name);
        setReturnReference(token.returnReference);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public List<ArgumentStmtToken> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgumentStmtToken> arguments) {
        this.arguments = arguments;
    }

    public ClassStmtToken getClazz() {
        return clazz;
    }

    public void setClazz(ClassStmtToken clazz) {
        this.clazz = clazz;
    }

    public boolean isInterfacable(){
        return body == null;
    }
}
