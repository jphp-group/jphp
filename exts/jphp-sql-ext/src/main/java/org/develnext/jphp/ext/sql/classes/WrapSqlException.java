package org.develnext.jphp.ext.sql.classes;

import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

@Name("SqlException")
@Reflection.Namespace(SqlExtension.NS)
public class WrapSqlException extends JavaException {
    public WrapSqlException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public WrapSqlException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
