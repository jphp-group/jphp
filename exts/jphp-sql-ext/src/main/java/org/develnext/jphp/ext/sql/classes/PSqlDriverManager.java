package org.develnext.jphp.ext.sql.classes;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Reflection.Name("SqlDriverManager")
@Reflection.Namespace(SqlExtension.NS)
final public class PSqlDriverManager extends BaseObject {
    public PSqlDriverManager(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {}

    protected final static Map<String, String> dataSourceClasses = new HashMap<String, String>() {{
        put("sqlite", "org.sqlite.javax.SQLiteConnectionPoolDataSource");

        put("mysql", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        put("psql", "org.postgresql.ds.PGPoolingDataSource");
        put("postgres", "org.postgresql.ds.PGPoolingDataSource");
        put("postgresql", "org.postgresql.ds.PGPoolingDataSource");

        put("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDataSource");

        put("sybase", "com.sybase.jdbc4.jdbc.SybDataSource");

        put("hsql", "org.hsqldb.jdbc.JDBCDataSource");

        put("firebird", "org.firebirdsql.pool.FBWrappingDataSource");
    }};

    protected final static Map<String, String> driverClasses = new HashMap<String, String>() {{
        put("db2", "COM.ibm.db2.jdbc.app.DB2Driver");
        put("mysql", "com.mysql.jdbc.Driver");
        put("psql", "org.postgresql.Driver");
        put("postgres", "org.postgresql.Driver");
        put("postgresql", "org.postgresql.Driver");
        put("postgresql", "org.postgresql.Driver");
        put("mssql", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
        put("sybase", "com.sybase.jdbc2.jdbc.SybDriver");
        put("firebird", "org.firebirdsql.jdbc.FBDriver");
        put("hsql", "org.hsql.jdbcDriver");
        put("interbase", "interbase.interclient.Driver");
        put("sqlite", "org.sqlite.JDBC");
    }};

    protected static String _getDriverClass(String dbName) {
        String driverClass = driverClasses.get(dbName.toLowerCase());

        if (driverClass == null) {
            driverClass = dbName;
        }

        return driverClass;
    }

    @Signature
    public static void install(String dbName) throws SQLException {
        String driverClass = _getDriverClass(dbName);

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver class '" + driverClass + "' is not found in classpath");
        }
    }

    @Signature
    public static PSqlConnectionPool getPool(Environment env, String url, String driverName) throws SQLException {
        return getPool(env, url, driverName, null);
    }

    @Signature
    public static PSqlConnectionPool getPool(Environment env, String url, String driverName, @Nullable Properties properties) throws SQLException {
        HikariConfig config = new HikariConfig(properties == null ? new Properties() : properties);

        if (config.getDataSourceClassName() == null) {
            config.setDataSourceClassName(dataSourceClasses.get(driverName));
        }

        HikariDataSource pool = new HikariDataSource(config);

        pool.setDriverClassName(_getDriverClass(driverName));
        pool.setJdbcUrl("jdbc:" + url);

        return new PSqlConnectionPool(env, pool);
    }

    @Signature
    public static PSqlConnection getConnection(Environment env, String url) throws SQLException {
        return getConnection(env, url, null);
    }

    @Signature
    public static PSqlConnection getConnection(Environment env, String url, @Nullable Properties properties) throws SQLException {
        return new PSqlConnection(env, DriverManager.getConnection("jdbc:" + url, properties == null ? new Properties() : properties));
    }
}
