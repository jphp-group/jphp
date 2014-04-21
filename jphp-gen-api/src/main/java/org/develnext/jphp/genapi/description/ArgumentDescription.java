package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ArgumentStmtToken;
import org.develnext.jphp.genapi.parameter.MethodParamParameter;
import php.runtime.common.HintType;

public class ArgumentDescription extends BaseDescription<ArgumentStmtToken> {
    protected MethodParamParameter description;
    protected String[] types;

    public ArgumentDescription(ArgumentStmtToken token, MethodParamParameter description) {
        super(token);
        this.description = description;
    }

    public String[] getTypes() {
        return types;
    }

    public boolean isReference() {
        return token.isReference();
    }

    public String getName() {
        return token.getName().getName();
    }

    public String getValue() {
        if (token.getValue() == null)
            return null;

        if (token.getValue().getLast() instanceof StringExprToken) {
            return "'" + token.getValue().getWord() + "'";
        } else {
            return token.getValue().getMeta().getWord();
        }
    }

    public String getDescription() {
        return description == null ? null : description.getDescription();
    }

    @Override
    protected void parse() {
        types = description == null ? null : description.getTypes();

        if (types == null) {
            if (token.getHintTypeClass() != null) {
                types = new String[] { token.getHintTypeClass().getName() };
            } else if (token.getHintType() != null && token.getHintType() != HintType.ANY) {
                types = new String[] { token.getHintType().toString() };
            }
        }
    }
}
