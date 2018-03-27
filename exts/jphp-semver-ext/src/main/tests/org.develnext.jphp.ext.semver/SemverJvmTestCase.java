package org.develnext.jphp.ext.semver;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.semver.SemverExtension;
import php.runtime.env.CompileScope;

public class SemverJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new SemverExtension());
        return scope;
    }
}
