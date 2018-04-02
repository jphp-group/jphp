package org.develnext.jphp.ext.$ns$;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.$ns$.$extClassName$;
import php.runtime.env.CompileScope;

public class $className$ extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new $extClassName$());
        return scope;
    }
}
