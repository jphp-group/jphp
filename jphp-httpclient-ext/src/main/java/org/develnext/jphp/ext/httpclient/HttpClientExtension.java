package org.develnext.jphp.ext.httpclient;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class HttpClientExtension extends Extension {
    public final static String NS = "php\\net";

    @Override
    public Status getStatus() {
        return Status.BETA;
    }

    @Override
    public void onRegister(CompileScope scope) {

    }
}
