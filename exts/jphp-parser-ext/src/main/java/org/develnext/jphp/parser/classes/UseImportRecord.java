package org.develnext.jphp.parser.classes;

import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Namespace(ParserExtension.NS)
public class UseImportRecord extends AbstractSourceRecord {
    protected String alias;

    public UseImportRecord(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public String getAlias() {
        return alias;
    }

    @Setter
    public void setAlias(String alias) {
        this.alias = alias;
    }
}
