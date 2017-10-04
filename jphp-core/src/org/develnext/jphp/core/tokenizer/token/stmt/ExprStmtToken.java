package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.compiler.common.ASMExpression;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.YieldExprToken;
import php.runtime.env.Context;
import php.runtime.env.Environment;

import java.util.Arrays;
import java.util.List;

public class ExprStmtToken extends StmtToken {
    private List<Token> tokens;
    private ExprStmtToken asmExpr;
    private boolean isStmtList = true;
    private boolean constantly = true;

    protected ExprStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public ExprStmtToken(Environment env, Context context, List<Token> tokens){
        this(TokenMeta.of(tokens));
        setTokens(tokens);

        if (!isStmtList() && env != null && context != null) {
            updateAsmExpr(env, context);
        }
    }

    public ExprStmtToken(Environment env, Context context, Token... tokens){
        this(env, context, Arrays.asList(tokens));
    }

    public void updateAsmExpr(Environment env, Context context) {
        if (!isStmtList()) {
            setAsmExpr(new ASMExpression(env, context, this).getResult());
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        for (Token el : tokens){
            if (el == null) continue;

            if (!(el instanceof StmtToken)) {
                isStmtList = false;
                break;
            }

            if (el instanceof GotoStmtToken || el instanceof LabelStmtToken) {
                constantly = false;
            }
        }

        this.tokens = tokens;
    }

    public boolean isSingle(){
        return tokens.size() == 1;
    }

    public Token getSingle(){
        return tokens.get(0);
    }

    public Token getLast(){
        if (tokens.size() == 0)
            return null;
        return tokens.get(tokens.size() - 1);
    }

    public boolean isStmtList() {
        return isStmtList;
    }

    public boolean isConstantly() {
        return constantly;
    }

    public ExprStmtToken getAsmExpr() {
        return asmExpr;
    }

    public void setAsmExpr(ExprStmtToken asmExpr) {
        this.asmExpr = asmExpr;
    }
}
