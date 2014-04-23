package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.token.CommentToken;
import php.runtime.common.Modifier;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

public class ClassVarStmtToken extends StmtToken {
    private boolean isStatic;
    private Modifier modifier;

    private VariableExprToken variable;
    private ExprStmtToken value;
    private CommentToken docComment;
    private ClassStmtToken clazz;

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

    public CommentToken getDocComment() {
        return docComment;
    }

    public void setDocComment(CommentToken docComment) {
        this.docComment = docComment;
    }

    public ClassStmtToken getClazz() {
        return clazz;
    }

    public void setClazz(ClassStmtToken clazz) {
        this.clazz = clazz;
    }
}
