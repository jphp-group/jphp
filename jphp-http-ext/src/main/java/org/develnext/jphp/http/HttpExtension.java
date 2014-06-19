package org.develnext.jphp.http;

import org.develnext.jphp.http.classes.WrapHttpClient;
import org.develnext.jphp.http.classes.WrapHttpRequest;
import org.develnext.jphp.http.classes.WrapHttpResponse;
import org.develnext.jphp.http.classes.entity.WrapHttpEntity;
import org.develnext.jphp.http.classes.entity.WrapHttpFileEntity;
import org.develnext.jphp.http.classes.entity.WrapHttpUrlEncodingFormEntity;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class HttpExtension extends Extension {
    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerNativeClass(scope, WrapHttpClient.class);
        registerNativeClass(scope, WrapHttpRequest.class);
        registerNativeClass(scope, WrapHttpResponse.class);
        registerNativeClass(scope, WrapHttpEntity.class);
        registerNativeClass(scope, WrapHttpFileEntity.class);
        registerNativeClass(scope, WrapHttpUrlEncodingFormEntity.class);
    }
}
