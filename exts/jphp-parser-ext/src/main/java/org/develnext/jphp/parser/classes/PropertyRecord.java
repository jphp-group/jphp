package org.develnext.jphp.parser.classes;

import org.develnext.jphp.core.tokenizer.token.stmt.ClassVarStmtToken;
import org.develnext.jphp.parser.ParserExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Namespace(ParserExtension.NS)
public class PropertyRecord extends AbstractSourceRecord<ClassVarStmtToken> {
    private String name;
    protected ClassRecord classRecord;
    protected boolean isStatic;

    public PropertyRecord(Environment env) {
        super(env);
    }

    public PropertyRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public ClassRecord getClassRecord() {
        return classRecord;
    }

    @Setter
    public void setClassRecord(ClassRecord classRecord) {
        this.classRecord = classRecord;
    }

    @Getter
    public String getName() {
        return name;
    }

    @Setter
    public void setName(String name) {
        this.name = name;
    }

    @Getter
    public boolean isStatic() {
        return isStatic;
    }

    @Setter
    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}
