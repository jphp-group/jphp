package org.develnext.jphp.genapi.parameter;

import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;

abstract public class BaseParameter {

    protected final String value;
    protected final NamespaceStmtToken namespace;

    public BaseParameter(NamespaceStmtToken namespace, String value) {
        this.namespace = namespace;
        this.value = value.trim().replace('\t', ' ');
        parse();
    }

    abstract protected void parse();
}
