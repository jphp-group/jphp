package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.Collection;
import java.util.Map;

@Namespace(ParserExtension.NS)
public class ClassRecord extends AbstractSourceRecord<ClassStmtToken> {
    public enum Type { CLASS, INTERFACE, TRAIT }

    protected NamespaceRecord namespaceRecord;

    protected ClassRecord parent;
    protected boolean shortParentName = false;

    protected Map<String, ClassRecord> interfaces;
    protected Map<String, MethodRecord> methodRecords;

    protected Type type = Type.CLASS;

    protected boolean isAbstract;

    public ClassRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public ClassRecord(Environment env) {
        super(env);
    }

    @Getter
    public boolean getShortParentName() {
        return shortParentName;
    }

    @Setter
    public void setShortParentName(boolean shortParentName) {
        this.shortParentName = shortParentName;
    }

    @Getter
    public boolean isAbstract() {
        return isAbstract;
    }

    @Setter
    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    @Getter
    public Type getType() {
        return type;
    }

    @Setter
    public void setType(Type type) {
        this.type = type;
    }

    @Getter
    public NamespaceRecord getNamespaceRecord() {
        return namespaceRecord;
    }

    @Setter
    public void setNamespaceRecord(NamespaceRecord namespaceRecord) {
        this.namespaceRecord = namespaceRecord;
    }

    @Getter
    public ClassRecord getParent() {
        return parent;
    }

    @Setter
    public void setParent(@Nullable ClassRecord parent) {
        this.parent = parent;
    }

    @Signature
    public void addInterface(ClassRecord record) {
        interfaces.put(record.getName().toLowerCase(), record);
    }

    @Signature
    public ClassRecord getInterface(String name) {
        return interfaces.get(name);
    }

    @Signature
    public void removeInterface(String name) {
        interfaces.remove(name);
    }

    @Signature
    public Collection<ClassRecord> getInterfaces() {
        return this.interfaces.values();
    }

    @Signature
    public void addMethod(MethodRecord record) {
        record.setClassRecord(this);
        methodRecords.put(record.getName().toLowerCase(), record);
    }

    @Signature
    public boolean hasMethod(String name) {
        return methodRecords.containsKey(name.toLowerCase());
    }

    @Signature
    public MethodRecord getMethod(String name) {
        return methodRecords.get(name);
    }

    @Signature
    public void removeMethod(String name) {
        MethodRecord record = methodRecords.remove(name.toLowerCase());
        record.setClassRecord(null);
    }

    @Signature
    public Collection<MethodRecord> getMethods() {
        return methodRecords.values();
    }

    @Override
    @Signature
    public void clear() {
        super.clear();

        methodRecords.clear();
        interfaces.clear();
        parent = null;
    }
}
