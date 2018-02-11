package org.develnext.jphp.json;

import org.develnext.jphp.json.classes.JsonProcessor;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.support.Extension;

public class JsonExtension extends Extension {
    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "std", "json" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, JsonSerializable.class);
        registerClass(scope, JsonProcessor.class);
    }

    @Override
    public void onLoad(Environment env) {
        WrapProcessor.registerCode(env, "json", JsonProcessor.class, JsonProcessor.DESERIALIZE_AS_ARRAYS);
    }
}
