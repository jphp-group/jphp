package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.compiler.common.ASMExpression;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.ArgumentUnpackExprToken;
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
    private boolean variadic = false;

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

    public static boolean isConstable(ExprStmtToken expr) {
        if (expr.isStmtList() || !expr.isConstantly()) {
            return false;
        }

        for (Token token : expr.getTokens()) {
            if (token instanceof OperatorExprToken || token instanceof BraceExprToken) continue;

            if (!ValueExprToken.isConstable(token, true)) {
                return false;
            }
        }

        return true;
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
        int cnt = 0;
        variadic = false;

        for (Token el : tokens) {
            if (el == null) continue;
            cnt++;

            if (cnt == 1 && el instanceof ArgumentUnpackExprToken) {
                variadic = true;
            }

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

    public boolean isVariadic() {
        return variadic;
    }

    public void setVariadic(boolean variadic) {
        this.variadic = variadic;
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
