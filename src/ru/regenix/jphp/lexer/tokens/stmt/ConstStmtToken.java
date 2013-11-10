package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.expr.value.NameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

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
