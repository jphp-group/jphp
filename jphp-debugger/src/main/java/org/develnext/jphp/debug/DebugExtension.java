package org.develnext.jphp.debug;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class DebugExtension extends Extension {
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        scope.setTickHandler(new DebugTickHandler());
    }
}
