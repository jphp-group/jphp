package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.MethodStmtToken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassDescription extends BaseDescription<ClassStmtToken> {
    protected Map<String, MethodDescription> methods;

    public ClassDescription(ClassStmtToken token) {
        super(token);
    }

    @Override
    protected void parse() {
        methods = new LinkedHashMap<String, MethodDescription>();
        for(MethodStmtToken el : token.getMethods()) {
            methods.put(el.getName().getName().toLowerCase(), new MethodDescription(el));
        }
    }

    public MethodDescription findMethod(String name) {
        return methods.get(name.toLowerCase());
    }

    public Collection<MethodDescription> getMethods() {
        return methods.values();
    }

    public String getName() {
        return token.getFulledName();
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
}
