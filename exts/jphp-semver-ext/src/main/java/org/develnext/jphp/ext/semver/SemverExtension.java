package org.develnext.jphp.ext.semver;

import org.develnext.jphp.ext.semver.classes.PSemVersion;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class SemverExtension extends Extension {
    public static final String NS = "semver";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PSemVersion.class);
    }
}
