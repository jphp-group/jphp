package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;

public class InstanceofExprToken extends OperatorExprToken {

    protected FulledNameToken what;

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
        return what;
    }

    public void setWhat(FulledNameToken what) {
        this.what = what;
    }
}
