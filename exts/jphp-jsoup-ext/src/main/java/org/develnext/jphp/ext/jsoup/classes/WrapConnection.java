package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;

import static org.jsoup.Connection.Request;
import static org.jsoup.Connection.Response;
import static php.runtime.annotation.Reflection.*;

@Abstract
@Name("Connection")
@Namespace(JsoupExtension.NS)
public class WrapConnection extends BaseWrapper<Connection> {
    public interface WrappedInterface {
        Connection proxy(Proxy proxy);
        Connection url(String url);
        Connection userAgent(String userAgent);
        Connection timeout(int millis);
        Connection maxBodySize(int bytes);
        Connection referrer(String referrer);
        Connection followRedirects(boolean followRedirects);
        Connection method(Connection.Method method);
        Connection ignoreHttpErrors(boolean ignoreHttpErrors);
        Connection ignoreContentType(boolean ignoreContentType);
        Connection data(Map<String, String> data);
        Connection header(String name, String value);
        Connection cookie(String name, String value);
        Connection cookies(Map<String, String> cookies);
        Document get() throws IOException;
        Document post() throws IOException;
        Response execute() throws IOException;
        Request request();
        Connection request(Request request);
        Response response();
        Connection response(Response response);
    }

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET  = "GET";

    public WrapConnection(Environment env, Connection object) {
        super(env, object);
    }

    public WrapConnection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Connection headers(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            getWrappedObject().header(entry.getKey(), entry.getValue());
        }

        return getWrappedObject();
    }
}
