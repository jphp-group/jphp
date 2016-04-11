package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.net.WrapURLConnection;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

@Name("HttpClient")
@Namespace(HttpClientExtension.NS)
public class PHttpClient extends BaseObject {
    protected int timeout;
    protected Proxy proxy;

    public PHttpClient(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "request", nativeType = PHttpRequest.class)
    })
    public Memory send(Environment env, Memory... args) throws Throwable {
        HttpURLConnection connection = (HttpURLConnection) makeConnection(args[0].toObject(PHttpRequest.class), env);

        PHttpResponse response = new PHttpResponse(env);
        env.invokeMethod(this, "applyConnection", ObjectMemory.valueOf(response), ObjectMemory.valueOf(new WrapURLConnection(env, connection)));

        return ObjectMemory.valueOf(response);
    }

    @Signature
    public int getTimeout() {
        return timeout;
    }

    @Signature
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Signature
    public Proxy getProxy() {
        return proxy;
    }

    @Signature
    public void setProxy(@Nullable Proxy proxy) {
        this.proxy = proxy;
    }

    @Signature
    protected void applyConnection(PHttpResponse response, URLConnection _connection, Environment env) throws IOException {
        if (_connection instanceof HttpURLConnection) {
            HttpURLConnection connection = (HttpURLConnection) _connection;

            response.setStatusCode(connection.getResponseCode());
            response.setStatusMessage(connection.getResponseMessage());

            try {
                response.setBodyStream(new MiscStream(env, connection.getInputStream()));
            } catch (IOException e) {
                response.setBodyStream(new MiscStream(env, connection.getErrorStream()));
            }

            // headers.
            ArrayMemory headers = new ArrayMemory();
            Map<String, List<String>> headerFields = connection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                String name = entry.getKey();
                List<String> value = entry.getValue();

                if (value.size() == 1) {
                    headers.putAsKeyString(name, StringMemory.valueOf(value.get(0)));
                } else {
                    headers.putAsKeyString(name, ArrayMemory.ofStringCollection(value));
                }
            }
            response.setHeaders(headers);


            ArrayMemory cookies = new ArrayMemory();
            String cookieHeader = connection.getHeaderField("Set-Cookie");

            if (cookieHeader != null && !cookieHeader.isEmpty()) {
                List<HttpCookie> httpCookies = HttpCookie.parse(cookieHeader);

                for (HttpCookie cookie : httpCookies) {
                    ArrayMemory memory = HttpClientExtension.cookieToArray(cookie);
                    cookies.putAsKeyString(cookie.getName(), memory);
                }
            }

            response.setRawCookies(cookies);
        } else {
            throw new IllegalStateException("Argument 2 must be instance of HttpURLConnection");
        }
    }

    @Signature
    protected URLConnection makeConnection(PHttpRequest request, Environment env) throws Throwable {
        URL url = new URL(request.getUrl());

        URLConnection connection = proxy == null ? url.openConnection() : url.openConnection(proxy);

        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setReadTimeout(timeout);
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setRequestMethod(request.getMethod());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            // cookies.
            ArrayMemory cookies = request.getCookies();
            ForeachIterator iterator = cookies.foreachIterator(false, false);
            StringBuilder cookieHeader = new StringBuilder();

            while (iterator.next()) {
                String name = iterator.getStringKey();
                String value = iterator.getValue().toString();

                cookieHeader.append(URLEncoder.encode(name, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8")).append(';');
            }

            httpURLConnection.setRequestProperty("Cookie", cookieHeader.toString());

            ArrayMemory headers = request.getHeaders();
            iterator = headers.foreachIterator(false, false);

            while (iterator.next()) {
                String name = iterator.getStringKey();
                Memory value = iterator.getValue();

                if (value.isTraversable()) {
                    ForeachIterator subIterator = value.getNewIterator(env);
                    while (subIterator.next()) {
                        httpURLConnection.addRequestProperty(name, subIterator.getValue().toString());
                    }
                } else {
                    httpURLConnection.addRequestProperty(name, value.toString());
                }
            }

            Memory body = request.getBody(env);

            if (body.instanceOf(PHttpBody.class)) {
                body.toObject(PHttpBody.class).apply(env, ObjectMemory.valueOf(new WrapURLConnection(env, connection)));
            } else {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(request.getBody(env).toBinaryString());
                writer.close();
            }

            return httpURLConnection;
        } else {
            throw new IllegalStateException("HttpClient::makeConnection() method must return instance of HttpURLConnection");
        }
    }
}
