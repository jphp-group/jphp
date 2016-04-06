package org.develnext.jphp.ext.httpclient.classes;

import org.develnext.jphp.ext.httpclient.HttpClientExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Name("HttpClient")
@Namespace(HttpClientExtension.NS)
public class PHttpClient extends BaseObject {
    protected int timeout;

    public PHttpClient(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg(value = "request", nativeType = PHttpRequest.class)
    })
    public Memory send(Environment env, Memory... args) throws IOException {
        HttpURLConnection connection = makeConnection(args[0].toObject(PHttpRequest.class), env);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
    protected HttpURLConnection makeConnection(PHttpRequest request, Environment env) throws IOException {
        URL url = new URL(request.getUrl());

        URLConnection connection = url.openConnection();

        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setReadTimeout(timeout);
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setRequestMethod(request.getMethod());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            ArrayMemory headers = request.getHeaders();
            ForeachIterator iterator = headers.foreachIterator(false, false);

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

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(request.getBody().toBinaryString());
            writer.close();

            return httpURLConnection;
        } else {
            throw new IllegalStateException("HttpClient::makeConnection() method must return instance of HttpURLConnection");
        }
    }
}
