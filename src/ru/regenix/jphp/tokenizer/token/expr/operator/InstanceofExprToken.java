package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;

public class InstanceofExprToken extends OperatorExprToken {

    protected Token what;

    public InstanceofExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INSTANCEOF);
    }

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    public boolean isBinary() {
        return false;
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

    public void setWhat(FulledNameToken what) {
        this.what = what;
    }

    public void setWhatVariable(VariableExprToken what){
        this.what = what;
    }
}
