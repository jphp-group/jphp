package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.Collection;

import static org.jsoup.Connection.Request;
import static php.runtime.annotation.Reflection.Name;

@Name(JsoupExtension.NAMESPACE + "ConnectionRequest")
@Reflection.WrapInterface(WrapConnectionRequest.Methods.class)
public class WrapConnectionRequest extends BaseWrapper<Request> {
    public WrapConnectionRequest(Environment env, Request object) {
        super(env, object);
    }

    public WrapConnectionRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public interface Methods {
        public int timeout();
        public Request timeout(int millis);

        public int maxBodySize();
        public Request maxBodySize(int bytes);

        public boolean followRedirects();
        public Request followRedirects(boolean followRedirects);

        public boolean ignoreHttpErrors();
        public Request ignoreHttpErrors(boolean ignoreHttpErrors);

        public boolean ignoreContentType();
        public Request ignoreContentType(boolean ignoreContentType);
    }
}
