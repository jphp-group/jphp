package org.develnext.jphp.ext.sql.classes;

import com.zaxxer.hikari.HikariDataSource;
import org.develnext.jphp.ext.sql.SqlExtension;
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
    protected HikariDataSource pool;

    public PSqlConnectionPool(Environment env, HikariDataSource pool) {
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
        pool.setUsername(name);
        return this;
    }

    @Signature
    public PSqlConnectionPool setPassword(String password) {
        pool.setPassword(password);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxPoolSize(int size) {
        pool.setMaximumPoolSize(size);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMaxLifetime(int time) {
        pool.setMaxLifetime(time);
        return this;
    }

    @Signature
    public PSqlConnectionPool setIdleTimeout(int time) {
        pool.setIdleTimeout(time);
        return this;
    }

    @Signature
    public PSqlConnectionPool setMinimumIdle(int time) {
        pool.setMinimumIdle(time);
        return this;
    }

    @Signature
    public PSqlConnection getConnection(Environment env) throws SQLException {
        return new PSqlConnection(env, pool.getConnection());
    }
}
