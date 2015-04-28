package org.develnext.jphp.ext.sql.classes;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.sql.SQLException;

@Name("SqlConnectionPool")
@Namespace(SqlExtension.NS)
public class PSqlConnectionPool extends BaseObject {
    protected AbstractComboPooledDataSource pool;

    public PSqlConnectionPool(Environment env, AbstractComboPooledDataSource pool) {
        super(env);
        this.pool = pool;
    }

    public PSqlConnectionPool(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected void __construct(PSqlConnectionPool parent) {
        pool = parent.pool;
    }

    @Signature
    public PSqlConnectionPool setUser(String name) {
        pool.setUser(name);
        return this;
    }

    @Signature
    public PSqlConnectionPool setPassword(String password) {
        pool.setPassword(password);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMinPoolSize(int size) {
        pool.setMinPoolSize(size);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxPoolSize(int size) {
        pool.setMaxPoolSize(size);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxStatements(int size) {
        pool.setMaxStatements(size);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxIdleTime(int time) {
        pool.setMaxIdleTime(time);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxConnectionAge(int age) {
        pool.setMaxConnectionAge(age);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxStatementsPerConnection(int value) {
        pool.setMaxStatementsPerConnection(value);
        return this;
    }

    @Signature
    public PSqlConnectionPool setInitialPoolSize(int value) {
        pool.setInitialPoolSize(value);
        return this;
    }

    @Signature
    public PSqlConnectionPool setAcquireIncrement(int value) {
        pool.setAcquireIncrement(value);
        return this;
    }

    @Signature
    public PSqlConnection getConnection(Environment env) throws SQLException {
        return new PSqlConnection(env, pool.getConnection());
    }
}
