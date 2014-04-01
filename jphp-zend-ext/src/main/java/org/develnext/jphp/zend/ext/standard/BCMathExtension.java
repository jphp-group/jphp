package org.develnext.jphp.zend.ext.standard;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class BCMathExtension extends Extension {
    @Override
    public String getName() {
        return "bcmath";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new BCMathFunctions());
    }
}
