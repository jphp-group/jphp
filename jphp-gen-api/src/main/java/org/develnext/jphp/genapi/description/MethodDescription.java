package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.MethodStmtToken;
import php.runtime.common.Modifier;

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

    public boolean isProtected() {
        return token.getModifier() == Modifier.PROTECTED;
    }

    public boolean isPrivate() {
        return token.getModifier() == Modifier.PRIVATE;
    }
}
