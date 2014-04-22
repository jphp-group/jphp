package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.StmtToken;

abstract public class BaseDescription<T extends StmtToken> {
    protected final T token;

    public BaseDescription(T token) {
        this.token = token;
        parse();
    }

    abstract protected void parse();
}
