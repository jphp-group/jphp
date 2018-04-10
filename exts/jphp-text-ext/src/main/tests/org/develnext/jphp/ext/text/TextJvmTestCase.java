package org.develnext.jphp.ext.text;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.text.TextExtension;
import php.runtime.env.CompileScope;

public class TextJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new TextExtension());
        return scope;
    }
}
