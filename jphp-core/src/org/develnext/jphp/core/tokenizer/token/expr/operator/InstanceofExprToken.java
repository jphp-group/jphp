package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import php.runtime.common.Association;

public class InstanceofExprToken extends OperatorExprToken {

    protected Token what;

    public InstanceofExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INSTANCEOF);
    }

    @Override
    public Association getOnlyAssociation() {
        return Association.LEFT;
    }

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    public ExprStmtToken getWhatExpr() {
        return what instanceof ExprStmtToken ? (ExprStmtToken)what : null;
    }

    public FulledNameToken getWhat() {
        return what instanceof FulledNameToken ? (FulledNameToken)what : null;
    }

    public VariableExprToken getWhatVariable(){
        return what instanceof VariableExprToken ? (VariableExprToken)what : null;
    }

    public boolean isVariable(){
        return what instanceof VariableExprToken;
    }

    public boolean isExpression() {
        return what instanceof ExprStmtToken;
    }

    public void setWhat(FulledNameToken what) {
        this.what = what;
    }

    public void setWhatVariable(VariableExprToken what){
        this.what = what;
    }

    public void setWhatExpr(ExprStmtToken what) {
        this.what = what;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
