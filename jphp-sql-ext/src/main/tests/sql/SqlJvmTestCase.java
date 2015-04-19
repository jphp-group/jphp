package sql;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.sql.SqlExtension;
import php.runtime.env.CompileScope;

public class SqlJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new SqlExtension());
        return scope;
    }
}
