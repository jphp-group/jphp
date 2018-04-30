package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Namespace(ParserExtension.NS)
public class MethodRecord extends AbstractSourceRecord<FunctionStmtToken> {
    protected NamespaceRecord namespaceRecord;
    protected ClassRecord classRecord;
    protected BodyRecord body;
    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected List<ArgumentRecord> argumentRecords;
    protected HintType returnTypeHint;
    protected String returnTypeHintClass;

    public MethodRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public MethodRecord(Environment env) {
        super(env);
    }

    @Getter
    public BodyRecord getBody() {
        return body;
    }

    @Setter
    public void setBody(BodyRecord body) {
        this.body = body;
    }

    @Getter
    public ClassRecord getClassRecord() {
        return classRecord;
    }

    public void setClassRecord(ClassRecord classRecord) {
        this.classRecord = classRecord;
    }

    @Getter
    public NamespaceRecord getNamespaceRecord() {
        return namespaceRecord;
    }

    public void setNamespaceRecord(NamespaceRecord namespaceRecord) {
        this.namespaceRecord = namespaceRecord;
    }

    @Getter
    public boolean isStatic() {
        return isStatic;
    }

    @Setter
    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    @Getter
    public boolean isFinal() {
        return isFinal;
    }

    @Setter
    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    @Getter
    public boolean isAbstract() {
        return isAbstract;
    }

    @Setter
    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Getter
    public HintType getReturnTypeHint() {
        return returnTypeHint;
    }

    @Setter
    public void setReturnTypeHint(HintType returnTypeHint) {
        this.returnTypeHint = returnTypeHint;
    }

    @Getter
    public String getReturnTypeHintClass() {
        return returnTypeHintClass;
    }

    @Setter
    public void setReturnTypeHintClass(String returnTypeHintClass) {
        this.returnTypeHintClass = returnTypeHintClass;
    }

    @Getter
    public List<ArgumentRecord> getArgumentRecords() {
        return argumentRecords;
    }

    @Setter
    public void setArgumentRecords(List<ArgumentRecord> argumentRecords) {
        this.argumentRecords = argumentRecords;
    }
}
