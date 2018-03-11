package org.develnext.jphp.parser.classes;

import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(ParserExtension.NS)
public class BodyRecord extends AbstractSourceRecord {
    protected String code;

    public BodyRecord(Environment env, String code) {
        super(env);
        this.setCode(code);
    }

    public BodyRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public String getCode() {
        return code;
    }

    @Setter
    public void setCode(String code) {
        this.code = code;
    }
}
