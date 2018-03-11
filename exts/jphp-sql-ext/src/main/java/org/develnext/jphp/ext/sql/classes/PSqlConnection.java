package org.develnext.jphp.ext.sql.classes;

import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;

import java.sql.*;

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
    protected DatabaseMetaData metaData;

    public PSqlConnection(Environment env, Connection connection) {
        super(env);
        this.connection = connection;
        try {
            this.metaData = connection.getMetaData();
        } catch (SQLException e) {
            throw new WrapSqlException(env, e);
        }
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
    public Memory getCatalogs(Environment env) throws SQLException {
        ResultSet catalogs = metaData.getCatalogs();
        ArrayMemory r = new ArrayMemory();

        while (catalogs.next()) {
            r.add(new PSqlResult(env, catalogs).toArray(env));
        }

        return r.toConstant();
    }

    @Signature
    public Memory getSchemas(Environment env) throws SQLException {
        ResultSet schemas = metaData.getSchemas();
        ArrayMemory r = new ArrayMemory();

        while (schemas.next()) {
            r.add(new PSqlResult(env, schemas).toArray(env));
        }

        return r.toConstant();
    }

    @Signature
    public Memory getMetaData() throws SQLException {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("userName").assign(metaData.getUserName());

        r.refOfIndex("driverName").assign(metaData.getDriverName());
        r.refOfIndex("driverVersion").assign(metaData.getDriverVersion());
        r.refOfIndex("databaseName").assign(metaData.getDatabaseProductName());
        r.refOfIndex("databaseVersion").assign(metaData.getDatabaseProductVersion());

        r.refOfIndex("catalogSeparator").assign(metaData.getCatalogSeparator());
        r.refOfIndex("catalogTerm").assign(metaData.getCatalogTerm());
        r.refOfIndex("schemaTerm").assign(metaData.getSchemaTerm());
        r.refOfIndex("procedureTerm").assign(metaData.getProcedureTerm());
        r.refOfIndex("searchStringEscape").assign(metaData.getSearchStringEscape());

        r.refOfIndex("numericFunctions").assign(metaData.getNumericFunctions());
        r.refOfIndex("stringFunctions").assign(metaData.getStringFunctions());
        r.refOfIndex("timeDateFunctions").assign(metaData.getTimeDateFunctions());
        r.refOfIndex("systemFunctions").assign(metaData.getSystemFunctions());

        r.refOfIndex("defaultTransactionIsolation").assign(metaData.getDefaultTransactionIsolation());
        r.refOfIndex("identifierQuoteString").assign(metaData.getIdentifierQuoteString());

        r.refOfIndex("maxBinaryLiteralLength").assign(metaData.getMaxBinaryLiteralLength());
        r.refOfIndex("maxCatalogNameLength").assign(metaData.getMaxCatalogNameLength());
        r.refOfIndex("maxCharLiteralLength").assign(metaData.getMaxCharLiteralLength());
        r.refOfIndex("maxConnections").assign(metaData.getMaxConnections());

        r.refOfIndex("maxColumnNameLength").assign(metaData.getMaxColumnNameLength());
        r.refOfIndex("maxColumnsInGroupBy").assign(metaData.getMaxColumnsInGroupBy());
        r.refOfIndex("maxColumnsInIndex").assign(metaData.getMaxColumnsInIndex());
        r.refOfIndex("maxColumnsInOrderBy").assign(metaData.getMaxColumnsInOrderBy());
        r.refOfIndex("maxColumnsInSelect").assign(metaData.getMaxColumnsInSelect());
        r.refOfIndex("maxColumnsInTable").assign(metaData.getMaxColumnsInTable());

        r.refOfIndex("maxCursorNameLength").assign(metaData.getMaxCursorNameLength());
        r.refOfIndex("maxIndexLength").assign(metaData.getMaxIndexLength());
        r.refOfIndex("maxProcedureNameLength").assign(metaData.getMaxProcedureNameLength());
        r.refOfIndex("maxRowSize").assign(metaData.getMaxRowSize());
        r.refOfIndex("maxSchemaNameLength").assign(metaData.getMaxSchemaNameLength());
        r.refOfIndex("maxStatementLength").assign(metaData.getMaxStatementLength());

        r.refOfIndex("maxTableNameLength").assign(metaData.getMaxTableNameLength());
        r.refOfIndex("maxTablesInSelect").assign(metaData.getMaxTablesInSelect());

        return r.toConstant();
    }

    @Signature
    public PSqlStatement query(Environment env, String sql) throws Throwable {
        return query(env, sql, null);
    }

    @Signature
    public PSqlStatement query(Environment env, String sql, @Nullable ArrayMemory args) throws Throwable {
        PreparedStatement statement = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
        PSqlStatement sqlStatement = new PSqlStatement(env, statement);

        if (args != null) {
            int index  = 0;

            for (ReferenceMemory arg : args) {
                env.invokeMethod(sqlStatement, "bind", LongMemory.valueOf(index), arg.getValue());
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
