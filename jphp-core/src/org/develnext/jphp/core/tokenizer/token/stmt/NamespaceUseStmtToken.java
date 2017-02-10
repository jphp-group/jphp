package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.TokenMeta;

public class NamespaceUseStmtToken extends StmtToken {
    public enum UseType { CLASS, FUNCTION, CONSTANT }

    private UseType useType = UseType.CLASS;
    private FulledNameToken name;
    private NameToken as;

    public NamespaceUseStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_USE);
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }

    public NameToken getAs() {
        return as;
    }

    public void setAs(NameToken as) {
        this.as = as;
    }

    public UseType getUseType() {
        return useType;
    }

    public void setUseType(UseType useType) {
        this.useType = useType;
    }

    public boolean isPackageImport() {
        if (!name.isSingle()) {
            return false;
        }

        NameToken token = name.getLastName();

        String name = token.getName();

        if (!name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
            return true;
        }

        return false;
    }
}
