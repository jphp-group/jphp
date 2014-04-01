package org.develnext.jphp.zend;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.zend.ext.ZendExtension;
import php.runtime.env.CompileScope;

public class ZendJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new ZendExtension());
        return scope;
    }
}
