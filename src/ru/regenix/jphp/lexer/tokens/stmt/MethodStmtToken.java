package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class MethodStmtToken extends StmtToken {
    private boolean isAbstract;
    private boolean isFinal;
    private boolean isStatic;

    private Modifier modifier;
    private boolean returnReference;

    private NameToken name;
    private ClassStmtToken clazz;
    private List<ArgumentStmtToken> arguments;
    private List<Token> body;

    public MethodStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNCTION);
    }

    public NameToken getName() {
        return name;
    }

    public void setName(NameToken name) {
        this.name = name;
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

    public List<Token> getBody() {
        return body;
    }

    public void setBody(List<Token> body) {
        this.body = body;
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

    public boolean isNop(){
        return body != null && body.isEmpty();
    }

    public boolean isReturnReference() {
        return returnReference;
    }

    public void setReturnReference(boolean returnReference) {
        this.returnReference = returnReference;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }
}
