package org.develnext.jphp.ext.mongo;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.mongo.MongoExtension;
import php.runtime.env.CompileScope;

public class MongoJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new MongoExtension());
        return scope;
    }
}
