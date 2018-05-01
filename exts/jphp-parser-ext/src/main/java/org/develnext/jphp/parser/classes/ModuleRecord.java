package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Namespace(ParserExtension.NS)
public class ModuleRecord extends AbstractSourceRecord {
    protected Map<String, NamespaceRecord> namespaces = new LinkedHashMap<>();

    public ModuleRecord(Environment env) {
        super(env);
    }

    public ModuleRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Collection<NamespaceRecord> getNamespaces() {
        return namespaces.values();
    }

    @Signature
    public Memory getClasses() {
        ArrayMemory result = ArrayMemory.createHashed();

        for (NamespaceRecord record : namespaces.values()) {
            Collection<ClassRecord> classes = record.getClasses();

            for (ClassRecord aClass : classes) {
                result.put(aClass.getName(), aClass);
            }
        }

        return result.toConstant();
    }

    @Signature
    public Memory getFunctions() {
        ArrayMemory result = ArrayMemory.createHashed();

        for (NamespaceRecord record : namespaces.values()) {
            Collection<MethodRecord> functions = record.getFunctions();

            for (MethodRecord function : functions) {
                result.put(function.getName(), function);
            }
        }

        return result.toConstant();
    }

    @Signature
    public ClassRecord findClass(String name) {
        for (NamespaceRecord record : namespaces.values()) {
            ClassRecord classRecord = record.getClass(name);

            if (classRecord != null) {
                return classRecord;
            }
        }

        return null;
    }

    @Signature
    public MethodRecord findFunction(String name) {
        return findMethod(name);
    }

    @Signature
    public MethodRecord findMethod(String name) {
        for (NamespaceRecord record : namespaces.values()) {
            MethodRecord function = record.getFunction(name);

            if (function != null) {
                return function;
            }
        }

        return null;
    }

    public NamespaceRecord synchronize(Environment env, NamespaceStmtToken namespace) {
        NamespaceRecord namespaceRecord;

        if (namespace != null && namespace.getName() != null) {
            namespaceRecord = namespaces.get(namespace.getName().toName().toLowerCase());
        } else {
            namespaceRecord = namespaces.get(null);
        }

        if (namespaceRecord == null) {
            namespaceRecord = new NamespaceRecord(env);
            namespaceRecord.setModuleRecord(this);
            namespaceRecord.setToken(namespace);
            namespaces.put(namespaceRecord.getIdName(), namespaceRecord);
        }

        return namespaceRecord;
    }

    public ClassRecord synchronize(Environment env, ClassStmtToken token, Tokenizer tokenizer) {
        NamespaceRecord namespaceRecord = synchronize(env, token.getNamespace());

        ClassRecord classRecord = namespaceRecord.getClass(token.getName().getName());

        if (classRecord == null) {
            classRecord = new ClassRecord(env);
            classRecord.setToken(token);
        }

        classRecord.setComment(token.getDocComment() == null ? null : token.getDocComment().getComment());
        classRecord.setName(token.getFulledName());
        classRecord.setIsAbstract(token.isAbstract());

        if (token.getExtend() != null) {
            ClassRecord parent = new ClassRecord(env);
            parent.setName(token.getExtend().getName().getName());
            classRecord.setParent(parent);
        }

        for (MethodStmtToken methodStmtToken : token.getMethods()) {
            synchronize(env, methodStmtToken, classRecord, tokenizer);
        }

        for (ClassVarStmtToken varStmtToken : token.getProperties()) {
            synchronize(env, varStmtToken, classRecord, tokenizer);
        }

        namespaceRecord.addClass(classRecord);

        return classRecord;
    }

    public void synchronize(Environment env, FunctionStmtToken token, Tokenizer tokenizer) {
        NamespaceRecord namespace = synchronize(env, token.getNamespace());

        MethodRecord methodRecord = namespace.getFunction(token.getFulledName());

        if (methodRecord == null) {
            methodRecord = new MethodRecord(env);
            methodRecord.setToken(token);
        }

        methodRecord.setName(token.getFulledName());
        methodRecord.setComment(token.getDocComment() == null ? null : token.getDocComment().getComment());

        namespace.addFunction(methodRecord);
    }

    public void synchronize(Environment env, ClassVarStmtToken varStmtToken, ClassRecord classRecord, Tokenizer tokenizer) {
        PropertyRecord classVar = classRecord.getClassVar(varStmtToken.getVariable().getName());

        if (classVar == null) {
            classVar = new PropertyRecord(env);
            classVar.setToken(varStmtToken);
        }

        classVar.setName(varStmtToken.getVariable().getName());
        classVar.setComment(varStmtToken.getDocComment() == null ? null : varStmtToken.getDocComment().getComment());
        classVar.setStatic(varStmtToken.isStatic());

        classRecord.addClassVar(classVar);
    }

    public void synchronize(Environment env, MethodStmtToken token, ClassRecord classRecord, Tokenizer tokenizer) {
        MethodRecord methodRecord = classRecord.getMethod(token.getName().getName());

        if (methodRecord == null) {
            methodRecord = new MethodRecord(env);
            methodRecord.setToken(token);
        }

        methodRecord.setName(token.getName().getName());
        methodRecord.setComment(token.getDocComment() == null ? null : token.getDocComment().getComment());
        methodRecord.setStatic(token.isStatic());
        methodRecord.setAbstract(token.isAbstract());
        methodRecord.setFinal(token.isFinal());
        methodRecord.setReturnTypeHint(token.getReturnHintType());
        methodRecord.setReturnTypeHintClass(token.getReturnHintTypeClass() == null ? null : token.getReturnHintTypeClass().getName());

        methodRecord.setArgumentRecords(new ArrayList<>(token.getArguments().size()));
        for (ArgumentStmtToken stmtToken : token.getArguments()) {
            methodRecord.getArgumentRecords().add(new ArgumentRecord(env, stmtToken));
        }

        classRecord.addMethod(methodRecord);
    }

    public void synchronize(Environment env, ConstStmtToken token, Tokenizer tokenizer) {

    }

    @Override
    @Signature
    public void clear() {
        super.clear();
        namespaces.clear();
    }
}
