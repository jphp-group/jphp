package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassVarStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.MethodStmtToken;
import org.develnext.jphp.genapi.DocAnnotations;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassDescription extends BaseDescription<ClassStmtToken> {
    protected Map<String, MethodDescription> methods;
    protected Map<String, PropertyDescription> properties;
    protected String description;

    public ClassDescription(ClassStmtToken token) {
        super(token);
    }

    @Override
    protected void parse() {
        methods = new LinkedHashMap<String, MethodDescription>();
        for(MethodStmtToken el : token.getMethods()) {
            methods.put(el.getName().getName().toLowerCase(), new MethodDescription(el));
        }

        properties = new LinkedHashMap<String, PropertyDescription>();
        for(ClassVarStmtToken el : token.getProperties()) {
            properties.put(el.getVariable().getName(), new PropertyDescription(el));
        }

        if (token.getDocComment() != null){
            DocAnnotations annotations = new DocAnnotations(token.getDocComment().getComment());
            description = annotations.getDescription();
        }
    }

    public MethodDescription findMethod(String name) {
        return methods.get(name.toLowerCase());
    }

    public Collection<MethodDescription> getMethods() {
        return methods.values();
    }

    public Collection<PropertyDescription> getProperties() {
        return properties.values();
    }

    public String getName() {
        return token.getFulledName();
    }

    public String getShortName() {
        return token.getName().getName();
    }

    public boolean isAbstract() {
        return token.isAbstract();
    }

    public boolean isFinal() {
        return token.isFinal();
    }

    public boolean isInterface() {
        return token.isInterface();
    }

    public boolean isTrait() {
        return token.isTrait();
    }

    public String getExtends() {
        return token.getExtend() == null ? null : token.getExtend().getName().getName();
    }

    public String[] getImplements() {
        if (token.getImplement() == null || token.getImplement().getNames().isEmpty())
            return null;

        String[] r = new String[token.getImplement().getNames().size()];
        for(int i = 0; i < r.length; i++) {
            r[i] = token.getImplement().getNames().get(i).getName();
        }

        return r;
    }

    public String getDescription() {
        return description;
    }
}
