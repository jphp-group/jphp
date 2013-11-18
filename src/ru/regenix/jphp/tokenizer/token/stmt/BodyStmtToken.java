package ru.regenix.jphp.tokenizer.token.stmt;

import com.google.common.collect.Lists;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

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

    public static BodyStmtToken of(ExprStmtToken... instructions){
        BodyStmtToken body = new BodyStmtToken(TokenMeta.of(instructions));
        body.setInstructions(Lists.newArrayList(instructions));
        return body;
    }

    public static BodyStmtToken of(List<ExprStmtToken> instructions) {
        BodyStmtToken body = new BodyStmtToken(TokenMeta.of(instructions));
        body.setInstructions(instructions);
        return body;
    }
}
