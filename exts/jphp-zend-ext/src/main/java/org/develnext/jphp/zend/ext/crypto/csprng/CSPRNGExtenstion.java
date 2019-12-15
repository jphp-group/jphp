package org.develnext.jphp.zend.ext.crypto.csprng;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class CSPRNGExtenstion extends Extension {
    @Override
    public String getName() {
        return "csprng";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public Status getStatus() {
        return Status.ZEND_LEGACY;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new CSPRNGFunctions());
    }
}
