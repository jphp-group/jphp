package org.develnext.jphp.ext.sql.classes;

import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Abstract
@Name("SqlConnection")
@Namespace(SqlExtension.NS)
public class PSqlConnection extends BaseObject {
    public static final int TRANSACTION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
    public static final int TRANSACTION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
    public static final int TRANSACTION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
    public static final int TRANSACTION_NONE = Connection.TRANSACTION_NONE;
    public static final int TRANSACTION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

    protected Connection connection;

    public PSqlConnection(Environment env, Connection connection) {
        super(env);
        this.connection = connection;
    }

    public PSqlConnection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Setter
    public void setAutoCommit(boolean value) throws SQLException {
        connection.setAutoCommit(value);
    }

    @Getter
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    @Setter
    public void setReadOnly(boolean value) throws SQLException {
        connection.setReadOnly(value);
    }

    @Getter
    public boolean getReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Setter
    public void setTransactionIsolation(int value) throws SQLException {
        connection.setTransactionIsolation(value);
    }

    @Getter
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Setter
    public void setCatalog(String value) throws SQLException {
        connection.setCatalog(value);
    }

    @Getter
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Signature
    public PSqlStatement query(Environment env, String sql) throws Throwable {
        return query(env, sql, null);
    }

    @Signature
    public PSqlStatement query(Environment env, String sql, @Nullable ArrayMemory args) throws Throwable {
        PreparedStatement statement = connection.prepareStatement(sql);
        PSqlStatement sqlStatement = new PSqlStatement(env, statement);

        if (args != null) {
            int index  = 0;

            for (ReferenceMemory arg : args) {
                env.invokeMethod(sqlStatement, "bind", LongMemory.valueOf(index), arg.value);
                index += 1;
            }
        }

        return sqlStatement;
    }

    @Signature
    public String identifier(String value) throws SQLException {
        String identifierQuoteString = connection.getMetaData().getIdentifierQuoteString();

        return identifierQuoteString + value + identifierQuoteString;
    }

    @Signature
    public void close() throws SQLException {
        connection.close();
    }

    @Signature
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Signature
    public void commit() throws SQLException {
        connection.commit();
    }
}
