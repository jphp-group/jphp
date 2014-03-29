package org.develnext.jphp.core.tokenizer.token.stmt;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import php.runtime.common.Modifier;
import php.runtime.reflection.ClassEntity;

import java.util.*;

public class ClassStmtToken extends StmtToken {
    private Modifier modifier;
    private boolean isFinal;
    private boolean isAbstract;

    private NamespaceStmtToken namespace;
    private NameToken name;
    private ExtendsStmtToken extend;
    private ImplementsStmtToken implement;

    private MethodStmtToken constructor;
    private List<ConstStmtToken> constants;
    private List<ClassVarStmtToken> properties;
    private List<MethodStmtToken> methods;
    private List<NameToken> uses;

    private Map<MethodName, List<Alias>> aliases;
    private Map<String, Replacement> replacements;

    private ClassEntity.Type classType = ClassEntity.Type.CLASS;

    protected ClassStmtToken(TokenMeta meta, TokenType type) {
        super(meta, type);
        properties = new ArrayList<ClassVarStmtToken>();
        constants = new ArrayList<ConstStmtToken>();
        methods = new ArrayList<MethodStmtToken>();
        uses = new ArrayList<NameToken>();
    }

    public ClassStmtToken(TokenMeta meta) {
        this(meta, TokenType.T_CLASS);
    }

    public boolean isInterface() {
        return classType == ClassEntity.Type.INTERFACE;
    }

    public void setInterface(boolean anInterface) {
        classType = anInterface ? ClassEntity.Type.INTERFACE : classType;
    }

    public boolean isTrait() { return classType == ClassEntity.Type.TRAIT; }

    public ClassEntity.Type getClassType() {
        return classType;
    }

    public void setClassType(ClassEntity.Type classType) {
        this.classType = classType;
    }

    public NameToken getName() {
        return name;
    }

    public void setName(NameToken name) {
        this.name = name;
    }

    public ExtendsStmtToken getExtend() {
        return extend;
    }

    public void setExtend(ExtendsStmtToken extend) {
        this.extend = extend;
    }

    public ImplementsStmtToken getImplement() {
        return implement;
    }

    public void setImplement(ImplementsStmtToken implement) {
        this.implement = implement;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public MethodStmtToken getConstructor() {
        return constructor;
    }

    public void setConstructor(MethodStmtToken constructor) {
        this.constructor = constructor;
    }

    public List<ConstStmtToken> getConstants() {
        return constants;
    }

    public void setConstants(List<ConstStmtToken> constants) {
        this.constants = constants;
    }

    public List<ClassVarStmtToken> getProperties() {
        return properties;
    }

    public void setProperties(List<ClassVarStmtToken> properties) {
        this.properties = properties;
    }

    public List<MethodStmtToken> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodStmtToken> methods) {
        this.methods = methods;
    }

    public NamespaceStmtToken getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceStmtToken namespace) {
        this.namespace = namespace;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public String getFulledName(char delimiter){
        return namespace == null || namespace.getName() == null
                ? name.getName()
                : namespace.getName().toName(delimiter) + delimiter + name.getName();
    }

    public String getFulledName(){
        return getFulledName('\\');
    }

    public List<NameToken> getUses() {
        return uses;
    }

    public void setUses(List<NameToken> uses) {
        this.uses = uses;
    }

    public void addAlias(String className, String methodName, Modifier modifier, String name) {
        if (aliases == null)
            aliases = new LinkedHashMap<MethodName, List<Alias>>();

        MethodName m = new MethodName(className, methodName);
        List<Alias> l = aliases.get(m);
        if (l == null) {
            aliases.put(m, l = new ArrayList<Alias>());
        }

        l.add(new Alias(modifier, name));
    }

    public List<Alias> findAliases(String className, String methodName) {
        return aliases == null ? null : aliases.get(new MethodName(className, methodName));
    }

    public boolean addReplacement(String className, String methodName, Set<String> classes) {
        if (classes == null || classes.isEmpty())
            throw new IllegalArgumentException("classes must not be null or empty");

        Set<String> tmp = new HashSet<String>();
        for(String e : classes)
            tmp.add(e.toLowerCase());

        if (replacements == null)
            replacements = new LinkedHashMap<String, Replacement>();

        Replacement replacement = replacements.get(methodName.toLowerCase());
        if (replacement == null) {
            replacement = new Replacement(className, tmp);
            replacements.put(methodName.toLowerCase(), replacement);
            return true;
        } else {
            for(String e : tmp) {
                if (replacement.hasTrait(e))
                    return false;
            }

            replacement.traits.addAll(tmp);
            return true;
        }
    }

    public Replacement findReplacement(String methodName) {
        return replacements == null ? null : replacements.get(methodName.toLowerCase());
    }

    protected static class MethodName {
        protected final String className;
        protected final String name;

        public MethodName(String className, String name) {
            this.className = className;
            this.name = name;
        }

        public String getClassName() {
            return className;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodName)) return false;

            MethodName that = (MethodName) o;

            if (!className.equalsIgnoreCase(that.className)) return false;
            if (!name.equalsIgnoreCase(that.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = className.toLowerCase().hashCode();
            result = 31 * result + name.toLowerCase().hashCode();
            return result;
        }
    }

    public static class Replacement {
        public String origin;
        public Set<String> traits;

        public Replacement(String origin, Set<String> traits) {
            this.origin = origin;
            this.traits = traits;
        }

        public boolean hasTrait(String name) {
            return traits.contains(name.toLowerCase());
        }
    }

    public static class Alias {
        protected final Modifier modifier;
        protected final String name;

        public Alias(Modifier modifier, String name) {
            this.modifier = modifier;
            this.name = name;
        }

        public Modifier getModifier() {
            return modifier;
        }

        public String getName() {
            return name;
        }
    }
}
