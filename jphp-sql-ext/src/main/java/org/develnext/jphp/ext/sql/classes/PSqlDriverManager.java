package org.develnext.jphp.ext.sql.classes;

import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Reflection.Name("SqlDriverManager")
@Reflection.Namespace(SqlExtension.NS)
final public class PSqlDriverManager extends BaseObject {
    public PSqlDriverManager(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {}

    @Signature
    public static void install(String dbName) throws SQLException {
        dbName = dbName.toLowerCase();

        String driverClass;

        if ("db2".equals(dbName)) {
            driverClass = "COM.ibm.db2.jdbc.app.DB2Driver";
        } else if ("mysql".equals(dbName)) {
            driverClass = "com.mysql.jdbc.Driver";
        } else if ("psql".equals(dbName) || "postgres".equals(dbName) || "postgresql".equals(dbName)) {
            driverClass = "org.postgresql.Driver";
        } else if ("mssql".equals(dbName)) {
            driverClass = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        } else if ("sybase".equals(dbName)) {
            driverClass = "ncom.sybase.jdbc2.jdbc.SybDriver";
        } else if ("firebird".equals(dbName)) {
            driverClass = "org.firebirdsql.jdbc.FBDriver";
        } else if ("hsql".equals(dbName)) {
            driverClass = "org.hsql.jdbcDriver";
        } else if ("interbase".equals(dbName)) {
            driverClass = "interbase.interclient.Driver";
        } else if ("sqlite".equals(dbName)) {
            driverClass = "org.sqlite.JDBC";
        } else {
            driverClass = dbName;
        }

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver class '" + driverClass + "' is not found in classpath");
        }
    }

    @Signature
    public static PSqlConnection getConnection(Environment env, String url) throws SQLException {
        return getConnection(env, url, null);
    }

    @Signature
    public static PSqlConnection getConnection(Environment env, String url, Properties properties) throws SQLException {
        return new PSqlConnection(env, DriverManager.getConnection("jdbc:" + url, properties == null ? new Properties() : properties));
    }
}
