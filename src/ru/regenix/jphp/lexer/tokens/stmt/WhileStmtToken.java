package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class WhileStmtToken extends StmtToken {
    private ExprStmtToken condition;

    public WhileStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_WHILE);
    }

    public ExprStmtToken getCondition() {
        return condition;
    }

    public void setCondition(ExprStmtToken condition) {
        this.condition = condition;
    }
}
