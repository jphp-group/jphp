package org.develnext.jphp.genapi.parameter;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;

public class MethodReturnParameter extends BaseParameter {
    protected String[] types;
    protected String description;

    public MethodReturnParameter(NamespaceStmtToken namespace, String value) {
        super(namespace, value);
    }

    protected int parseTypes(int offset) {
        int k = value.indexOf(' ', offset);
        String tmp;
        if (k == -1) {
            tmp = value;
            k = value.length() - 1;
        } else {
            tmp = value.substring(offset, k);
            k = k + 1;
        }

        String types[] = StringUtils.split(tmp, '|');
        this.types = new String[types.length];
        int i = -1;
        for(String el : types) {
            i++;
            el = el.trim();
            if (HintType.of(el) == null)
                el = SyntaxAnalyzer.getRealName(NameToken.valueOf(el.trim()), namespace).getName();

            this.types[i] = el;
        }

        return k;
    }

    @Override
    protected void parse() {
        int i = parseTypes(0);
        if (i < value.length())
            description = value.substring(i);
        else
            description = "";
    }

    public String[] getTypes() {
        return types;
    }

    public String getDescription() {
        return description;
    }
}
