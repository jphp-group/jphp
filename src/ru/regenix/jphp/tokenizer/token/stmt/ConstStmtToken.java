package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class ConstStmtToken extends StmtToken {
    private ClassStmtToken clazz;
    private NameToken name;
    private ExprStmtToken value;

    public ConstStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_CONST);
    }

    public NameToken getName() {
        return name;
    }

    public void setName(NameToken name) {
        this.name = name;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }

    public ClassStmtToken getClazz() {
        return clazz;
    }

    public void setClazz(ClassStmtToken clazz) {
        this.clazz = clazz;
    }
}
