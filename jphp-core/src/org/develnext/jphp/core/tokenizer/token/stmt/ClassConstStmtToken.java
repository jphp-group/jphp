package org.develnext.jphp.core.tokenizer.token.stmt;

import php.runtime.common.Modifier;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class ClassConstStmtToken extends ConstStmtToken {
    private Modifier modifier;
    private ClassStmtToken clazz;

    public ClassConstStmtToken(TokenMeta meta) {
        super(meta);
    }

    public ClassStmtToken getClazz() {
        return clazz;
    }

    public void setClazz(ClassStmtToken clazz) {
        this.clazz = clazz;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }
}
