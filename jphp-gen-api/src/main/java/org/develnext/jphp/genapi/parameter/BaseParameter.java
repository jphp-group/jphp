package org.develnext.jphp.genapi.parameter;

import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;
import php.runtime.common.HintType;

abstract public class BaseParameter {

    protected final String value;
    protected final NamespaceStmtToken namespace;

    public BaseParameter(NamespaceStmtToken namespace, String value) {
        this.namespace = namespace;
        this.value = value.trim().replace('\t', ' ');
        parse();
    }

    abstract protected void parse();

    protected boolean isNotClass(String type) {
        String ref = type;
        if (ref.endsWith("[]"))
            ref = type.substring(0, type.length() - 2);

        return HintType.of(ref) != null
                || ref.equalsIgnoreCase("mixed")
                || ref.equalsIgnoreCase("void")
                || ref.equalsIgnoreCase("null")
                || ref.equalsIgnoreCase("numeric");
    }
}
