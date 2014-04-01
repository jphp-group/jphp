package org.develnext.jphp.json;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class JsonExtension extends Extension {
    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerNativeClass(scope, JsonSerializable.class);
    }
}
