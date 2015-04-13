package org.develnext.jphp.json;

import org.develnext.jphp.json.classes.JsonProcessor;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class JsonExtension extends Extension {
    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, JsonSerializable.class);
        registerClass(scope, JsonProcessor.class);
    }
}
