package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ext.crypto.csprng.CSPRNGFunctions;
import org.develnext.jphp.zend.ext.json.JsonConstants;
import org.develnext.jphp.zend.ext.json.JsonFunctions;
import php.runtime.Startup;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class StandardExtension extends Extension {

    @Override
    public Status getStatus() {
        return Status.ZEND_LEGACY;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerConstants(new LangConstants());
        registerConstants(new StringConstants());
        registerConstants(new ArrayConstants());
        registerConstants(new FileConstants());
        registerConstants(new JsonConstants());
        registerConstants(new DateConstants());

        registerFunctions(new LangFunctions());
        registerFunctions(new StringFunctions());
        registerFunctions(new ArrayFunctions());
        registerFunctions(new FileFunctions());
        registerFunctions(new JsonFunctions());
        registerFunctions(new DateFunctions());
        registerFunctions(new CSPRNGFunctions());
    }
}
