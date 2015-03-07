package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.*;
import java.util.*;

@Abstract
@Name(FrameworkExtension.NS + "web\\HttpServletRequest")
public class WrapHttpServletRequest extends BaseWrapper<HttpServletRequest> {
    interface WrappedInterface {
        @Property String authType();
        @Property String method();
        @Property String pathInfo();
        @Property String pathTranslated();
        @Property String contextPath();
        @Property String queryString();
        @Property String remoteUser();
        @Property String requestedSessionId();
        @Property String requestURI();
        @Property String servletPath();

        @Property Collection<Part> parts();
        Part getPart(String name);

        String getHeader(String name);
        boolean isUserInRole(String role);

        HttpSession getSession(boolean create);
        HttpSession getSession();
        boolean isRequestedSessionIdValid();
        boolean isRequestedSessionIdFromURL();
        boolean isRequestedSessionIdFromCookie();

        boolean authenticate(HttpServletResponse response);
        void login(String username, String password);
        void logout();
    }

    public WrapHttpServletRequest(Environment env, HttpServletRequest wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpServletRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public List<Cookie> getCookies() {
        return Arrays.asList(getWrappedObject().getCookies());
    }

    @Signature
    public Memory getHeaders(String name) {
        return enumeration(getWrappedObject().getHeaders(name)).toConstant();
    }

    @Signature
    public Memory getHeaderNames() {
        return enumeration(getWrappedObject().getHeaderNames()).toConstant();
    }

    protected static ArrayMemory enumeration(Enumeration<String> enumeration) {
        ArrayMemory result = new ArrayMemory();

        while (enumeration.hasMoreElements()) {
            result.add(enumeration.nextElement());
        }

        return result;
    }
}
