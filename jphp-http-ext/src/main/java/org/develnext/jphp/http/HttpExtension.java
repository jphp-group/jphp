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
        registerClass(scope, WrapHttpClient.class);
        registerClass(scope, WrapHttpRequest.class);
        registerClass(scope, WrapHttpResponse.class);
        registerClass(scope, WrapHttpEntity.class);
        registerClass(scope, WrapHttpFileEntity.class);
        registerClass(scope, WrapHttpUrlEncodingFormEntity.class);
    }
}
