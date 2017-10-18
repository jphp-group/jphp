package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ArrayExprToken extends CallExprToken {
    private boolean listSyntax;
    private boolean shortSyntax;

    public ArrayExprToken(TokenMeta meta) {
        super(meta);
        this.setName(new NameToken(meta));
    }

    public boolean isListSyntax() {
        return listSyntax;
    }

    public void setListSyntax(boolean listSyntax) {
        this.listSyntax = listSyntax;
    }

    public boolean isShortSyntax() {
        return shortSyntax;
    }

    public void setShortSyntax(boolean shortSyntax) {
        this.shortSyntax = shortSyntax;
    }
}
