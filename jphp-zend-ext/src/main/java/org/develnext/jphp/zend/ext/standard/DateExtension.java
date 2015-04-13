package org.develnext.jphp.zend.ext.standard;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class DateExtension extends Extension {
    @Override
    public String getName() {
        return "date";
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
        registerFunctions(new DateFunctions());
    }
}
