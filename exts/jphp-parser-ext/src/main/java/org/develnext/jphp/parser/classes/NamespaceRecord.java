package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.*;

@Namespace(ParserExtension.NS)
public class NamespaceRecord extends AbstractSourceRecord<Token> {
    protected Map<String, MethodRecord> functions = new LinkedHashMap<>();
    protected Map<String, ClassRecord> classes = new LinkedHashMap<>();
    protected Set<UseImportRecord> useImports = new TreeSet<>();

    protected ModuleRecord moduleRecord;

    public NamespaceRecord(Environment env) {
        super(env);
    }

    public NamespaceRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public ModuleRecord getModuleRecord() {
        return moduleRecord;
    }

    @Setter
    public void setModuleRecord(ModuleRecord moduleRecord) {
        this.moduleRecord = moduleRecord;
    }

    @Signature
    public boolean isGlobally() {
        return name == null || name.isEmpty();
    }

    @Signature
    public void addUseImport(UseImportRecord record) {
        useImports.add(record);
    }

    @Signature
    public void addFunction(MethodRecord record) {
        record.setClassRecord(null);
        functions.put(record.getIdName(), record);
    }

    @Signature
    public MethodRecord getFunction(String name) {
        return functions.get(name);
    }

    @Signature
    public void addClass(ClassRecord record) {
        record.setNamespaceRecord(this);
        classes.put(record.getIdName(), record);
    }

    @Signature
    public ClassRecord getClass(String name) {
        return classes.get(name.toLowerCase());
    }

    @Signature
    public Collection<ClassRecord> getClasses() {
        return classes.values();
    }

    @Signature
    public Collection<MethodRecord> getFunctions() {
        return functions.values();
    }

    @Signature
    public Collection<UseImportRecord> getUseImports() {
        return useImports;
    }

    @Override
    @Signature
    public void clear() {
        super.clear();

        functions.clear();
        classes.clear();
        useImports.clear();
    }
}
