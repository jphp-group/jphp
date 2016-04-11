package org.develnext.jphp.ext.httpclient;

import org.develnext.jphp.ext.httpclient.classes.PHttpClient;
import org.develnext.jphp.ext.httpclient.classes.PHttpMessage;
import org.develnext.jphp.ext.httpclient.classes.PHttpRequest;
import org.develnext.jphp.ext.httpclient.classes.PHttpResponse;
import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.ArrayMemory;

import java.net.HttpCookie;

public class HttpClientExtension extends Extension {
    public final static String NS = "php\\net";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PHttpMessage.class);
        registerClass(scope, PHttpRequest.class);
        registerClass(scope, PHttpResponse.class);
        registerClass(scope, PHttpClient.class);
    }

    public static ArrayMemory cookieToArray(HttpCookie cookie) {
        ArrayMemory memory = new ArrayMemory();

        memory.refOfIndex("value").assign(cookie.getValue());
        memory.refOfIndex("name").assign(cookie.getName());
        memory.refOfIndex("domain").assign(cookie.getDomain());
        memory.refOfIndex("path").assign(cookie.getPath());
        memory.refOfIndex("secure").assign(cookie.getSecure());
        memory.refOfIndex("maxAge").assign(cookie.getMaxAge());
        memory.refOfIndex("comment").assign(cookie.getComment());
        memory.refOfIndex("discard").assign(cookie.getDiscard());
        memory.refOfIndex("ports").assign(cookie.getPortlist());
        memory.refOfIndex("version").assign(cookie.getVersion());
        memory.refOfIndex("httpOnly").assign(cookie.isHttpOnly());

        return memory;
    }

    public static HttpCookie arrayToCookie(ArrayMemory array) {
        Memory name = array.valueOfIndex("name");

        if (!name.toBoolean()) {
            throw new IllegalArgumentException("Cookie name is empty");
        }

        HttpCookie cookie = new HttpCookie(name.toString(), array.valueOfIndex("value").toString());

        if (array.containsKey("domain")) {
            cookie.setDomain(array.valueOfIndex("domain").toString());
        }

        if (array.containsKey("path")) {
            cookie.setPath(array.valueOfIndex("path").toString());
        }

        if (array.containsKey("secure")) {
            cookie.setSecure(array.valueOfIndex("secure").toBoolean());
        }

        if (array.containsKey("maxAge")) {
            cookie.setMaxAge(array.valueOfIndex("maxAge").toLong());
        }

        if (array.containsKey("comment")) {
            cookie.setComment(array.valueOfIndex("comment").toString());
        }

        if (array.containsKey("discard")) {
            cookie.setDiscard(array.valueOfIndex("discard").toBoolean());
        }

        if (array.containsKey("ports")) {
            cookie.setPortlist(array.valueOfIndex("ports").toString());
        }

        if (array.containsKey("version")) {
            cookie.setVersion(array.valueOfIndex("version").toInteger());
        }

        if (array.containsKey("httpOnly")) {
            cookie.setHttpOnly(array.valueOfIndex("httpOnly").toBoolean());
        }

        return cookie;
    }
}
