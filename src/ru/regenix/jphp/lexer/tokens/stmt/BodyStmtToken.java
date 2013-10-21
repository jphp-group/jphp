package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class BodyStmtToken extends StmtToken {

    private List<ExprStmtToken> instructions;

    public BodyStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public List<ExprStmtToken> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<ExprStmtToken> instructions) {
        this.instructions = instructions;
    }
}
