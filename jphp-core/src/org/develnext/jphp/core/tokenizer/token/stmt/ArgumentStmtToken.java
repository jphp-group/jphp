package org.develnext.jphp.core.tokenizer.token.stmt;

import php.runtime.common.HintType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;

public class ArgumentStmtToken extends StmtToken {
    private boolean reference;
    private boolean variadic;
    private HintType hintType;
    private NameToken hintTypeClass;
    private VariableExprToken name;
    private ExprStmtToken value;
    private boolean optional;

    public ArgumentStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public NameToken getHintTypeClass() {
        return hintTypeClass;
    }

    public HintType getHintType() {
        return hintType;
    }

    public void setHintType(HintType hintType) {
        this.hintType = hintType;
    }

    public void setHintTypeClass(NameToken typeHint) {
        this.hintTypeClass = typeHint;
    }

    public VariableExprToken getName() {
        return name;
    }

    public void setName(VariableExprToken name) {
        this.name = name;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public boolean isVariadic() {
        return variadic;
    }

    public void setVariadic(boolean variadic) {
        this.variadic = variadic;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
