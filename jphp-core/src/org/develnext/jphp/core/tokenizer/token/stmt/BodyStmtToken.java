package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

import java.util.Arrays;
import java.util.List;

public class BodyStmtToken extends StmtToken {
    private Boolean constantly;
    private boolean alternativeSyntax = false;
    private List<ExprStmtToken> instructions;

    public BodyStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public List<ExprStmtToken> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<ExprStmtToken> instructions) {
        this.instructions = instructions;
        this.constantly = null;
    }

    public static BodyStmtToken of(ExprStmtToken... instructions){
        BodyStmtToken body = new BodyStmtToken(TokenMeta.of(instructions));
        body.setInstructions(Arrays.asList(instructions));
        return body;
    }

    public static BodyStmtToken of(List<ExprStmtToken> instructions) {
        BodyStmtToken body = new BodyStmtToken(TokenMeta.of(instructions));
        body.setInstructions(instructions);
        return body;
    }

    public boolean isAlternativeSyntax() {
        return alternativeSyntax;
    }

    public void setAlternativeSyntax(boolean alternativeSyntax) {
        this.alternativeSyntax = alternativeSyntax;
    }

    public boolean isConstantly() {
        if (constantly != null)
            return constantly;

        for(ExprStmtToken e : instructions)
            if (!e.isConstantly())
                return constantly = false;
        return constantly = true;
    }

}
