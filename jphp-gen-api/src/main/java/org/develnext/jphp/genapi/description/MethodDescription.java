package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.MethodStmtToken;

public class MethodDescription extends FunctionDescription<MethodStmtToken> {

    public MethodDescription(MethodStmtToken token) {
        super(token);
    }

    public boolean isStatic() {
        return token.isStatic();
    }

    public boolean isFinal() {
        return token.isFinal();
    }

    public boolean isAbstract() {
        return token.isAbstract();
    }

    public boolean isInterfacable() {
        return token.isInterfacable();
    }
}
