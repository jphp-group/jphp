package org.develnext.jphp.json;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import php.runtime.env.CompileScope;

public class JsonJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new JsonExtension());
        return scope;
    }
}
