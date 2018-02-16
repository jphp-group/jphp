package org.develnext.jphp.yaml;

import org.develnext.jphp.yaml.classes.YamlProcessor;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.support.Extension;

public class YamlExtension extends Extension {
    @Override
    public Status getStatus() {
        return Status.BETA;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "std", "yaml" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, YamlProcessor.class);
    }

    @Override
    public void onLoad(Environment env) {
        WrapProcessor.registerCode(env, "yaml", YamlProcessor.class);
        WrapProcessor.registerCode(env, "yml", YamlProcessor.class);
    }
}
