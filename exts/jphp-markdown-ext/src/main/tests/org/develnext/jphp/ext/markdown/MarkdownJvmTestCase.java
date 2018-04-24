package org.develnext.jphp.ext.markdown;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.markdown.MarkdownExtension;
import php.runtime.env.CompileScope;

public class MarkdownJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new MarkdownExtension());
        return scope;
    }
}
