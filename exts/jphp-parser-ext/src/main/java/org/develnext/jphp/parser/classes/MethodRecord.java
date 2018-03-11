package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(ParserExtension.NS)
public class MethodRecord extends AbstractSourceRecord<FunctionStmtToken> {
    protected NamespaceRecord namespaceRecord;
    protected ClassRecord classRecord;
    protected BodyRecord body;

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
}
