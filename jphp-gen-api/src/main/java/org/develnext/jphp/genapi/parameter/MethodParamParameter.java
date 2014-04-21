package org.develnext.jphp.genapi.parameter;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;

/**
 * @param
 */
public class MethodParamParameter extends BaseParameter {
    protected String[] types;
    protected String argument;
    protected String description;

    public MethodParamParameter(NamespaceStmtToken namespace, String value) {
        super(namespace, value);
    }

    protected int parseArgument(int offset) {
        int k = value.indexOf(' ', offset);
        if (k == -1) {
            argument = value;
            return value.length() - 1;
        } else {
            argument = value.substring(offset, k);
            return k + 1;
        }
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
            if (HintType.of(el) == null
                    && !el.equalsIgnoreCase("mixed") && !el.equalsIgnoreCase("void") && !el.equalsIgnoreCase("null"))
                el = SyntaxAnalyzer.getRealName(NameToken.valueOf(el.trim()), namespace).getName();

            this.types[i] = el;
        }

        return k;
    }

    @Override
    protected void parse() {
        int i = 0;
        if (value.startsWith("$")) {
            i = parseArgument(i + 1);
        } else {
            i = parseTypes(i);
            i = parseArgument(i);
        }

        if (i < value.length())
            description = value.substring(i);
        else
            description = "";
    }

    public String[] getTypes() {
        return types;
    }

    public String getArgument() {
        return argument;
    }

    public String getDescription() {
        return description;
    }
}
