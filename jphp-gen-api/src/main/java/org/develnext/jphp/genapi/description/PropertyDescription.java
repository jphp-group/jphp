package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassVarStmtToken;
import org.develnext.jphp.genapi.DocAnnotations;
import org.develnext.jphp.genapi.parameter.BaseParameter;
import php.runtime.common.Modifier;

import java.util.ArrayList;
import java.util.List;

public class PropertyDescription extends BaseDescription<ClassVarStmtToken> {
    private String description;
    private boolean isReadonly;
    private String[] types;

    public PropertyDescription(ClassVarStmtToken token) {
        super(token);
    }

    @Override
    protected void parse() {
        if (token.getDocComment() != null){
            DocAnnotations annotations = new DocAnnotations(token.getDocComment().getComment());
            description = annotations.getDescription();
            isReadonly = annotations.hasParameter("readonly");

            List<String> types = new ArrayList<String>();
            for(String el : annotations.getParameter("var").values()) {
                el = el.trim();
                if (!BaseParameter.isNotClass(el))
                    el = SyntaxAnalyzer.getRealName(
                            NameToken.valueOf(el.trim()), token.getClazz().getNamespace()
                    ).getName();
                types.add(el);
            }

            this.types = types.toArray(new String[0]);
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public String[] getTypes() {
        return types;
    }

    public boolean isPrivate() {
        return token.getModifier() == Modifier.PRIVATE;
    }

    public boolean isProtected() {
        return token.getModifier() == Modifier.PROTECTED;
    }

    public boolean isStatic() {
        return token.isStatic();
    }

    public String getName() {
        return token.getVariable().getName();
    }

    public String getValue() {
        return token.getValue() == null ? null : token.getValue().getWord();
    }
}
