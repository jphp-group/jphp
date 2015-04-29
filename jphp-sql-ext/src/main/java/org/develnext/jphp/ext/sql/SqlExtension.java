package org.develnext.jphp.ext.sql;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.develnext.jphp.ext.sql.classes.*;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

import java.sql.SQLException;

public class SqlExtension extends Extension {
    public static final String NS = "php\\sql";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerJavaException(scope, WrapSqlException.class, SQLException.class);

        registerClass(scope, PSqlResult.class);
        registerClass(scope, PSqlStatement.class);
        registerClass(scope, PSqlConnection.class);
        registerClass(scope, PSqlConnectionPool.class);
        registerClass(scope, PSqlDriverManager.class);
    }
}
