package org.develnext.jphp.zend.ext.standard;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class StandardExtension extends Extension {
    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerConstants(new StringConstants());
        registerFunctions(new StringFunctions());

        registerConstants(new ArrayConstants());
        registerFunctions(new ArrayFunctions());

        registerConstants(new FileConstants());
        registerFunctions(new FileFunctions());
    }
}
