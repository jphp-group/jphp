package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.List;

public class ArrayGetRefExprToken extends ArrayGetExprToken {
    private List<ExprStmtToken> parameters;
    private boolean isShortcut = false;

    public ArrayGetRefExprToken(ArrayGetExprToken token){
        super(token.getMeta());
        parameters = token.getParameters();
    }

    public boolean isShortcut() {
        return isShortcut;
    }

    public void setShortcut(boolean shortcut) {
        isShortcut = shortcut;
    }

    @Override
    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
