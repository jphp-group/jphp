package org.develnext.jphp.http;

import org.develnext.jphp.http.classes.*;
import org.develnext.jphp.http.classes.entity.WrapHttpEntity;
import org.develnext.jphp.http.classes.entity.WrapHttpFileEntity;
import org.develnext.jphp.http.classes.entity.WrapHttpUrlEncodingFormEntity;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class NetExtension extends Extension {
    public final static String NAMESPACE = "php\\net\\";

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

        registerWrapperClass(scope, Proxy.class, WrapProxy.class);
        registerWrapperClass(scope, URLConnection.class, WrapURLConnection.class);
        registerWrapperClass(scope, URL.class, WrapURL.class);
    }
}
