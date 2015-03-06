package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Abstract
@Name(FrameworkExtension.NS + "web\\HttpServletResponse")
public class WrapHttpServletResponse extends BaseWrapper<HttpServletResponse> {
    interface WrappedInterface {
        void addCookie(Cookie cookie);

        boolean containsHeader(String name);

        String encodeURL(String url);
        String encodeRedirectURL(String url);

        void sendError(int sc, String msg);
        void sendError(int sc);
        void sendRedirect(String location);

        void setDateHeader(String name, long date);
        void addDateHeader(String name, long date);

        void setHeader(String name, String value);
        void addHeader(String name, String value);
        String getHeader(String name);
        Collection<String> getHeaders(String name);
        Collection<String> getHeaderNames();

        void setStatus(int sc);
        void setStatus(int sc, String sm);
        int getStatus();
    }

    public WrapHttpServletResponse(Environment env, HttpServletResponse wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpServletResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
