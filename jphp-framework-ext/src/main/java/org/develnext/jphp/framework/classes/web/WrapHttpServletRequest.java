package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.HttpServletRequest;

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
    }

    public WrapHttpServletRequest(Environment env, HttpServletRequest wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpServletRequest(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
