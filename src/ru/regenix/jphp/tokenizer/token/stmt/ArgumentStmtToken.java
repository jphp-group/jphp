package ru.regenix.jphp.tokenizer.token.stmt;

import php.runtime.common.HintType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

public class ArgumentStmtToken extends StmtToken {
    private boolean reference;
    private HintType hintType;
    private NameToken hintTypeClass;
    private VariableExprToken name;
    private ExprStmtToken value;

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
}
