package org.develnext.jphp.ext.compress;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.env.CompileScope;

public class CompressJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new CompressExtension());
        return scope;
    }
}
