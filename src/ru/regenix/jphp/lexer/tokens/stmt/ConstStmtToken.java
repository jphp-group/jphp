package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ConstStmtToken extends StmtToken {
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
}
