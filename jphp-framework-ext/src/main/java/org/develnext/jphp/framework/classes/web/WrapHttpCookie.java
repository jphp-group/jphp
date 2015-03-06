package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Cookie;

@Name(FrameworkExtension.NS + "web\\HttpCookie")
public class WrapHttpCookie extends BaseWrapper<Cookie> {
    interface WrappedInterface {
        @Property String domain();
        @Property int version();
        @Property boolean secure();
        @Property String path();
        @Property int maxAge();
        @Property String comment();
        @Property boolean httpOnly();

        @Property String value();
        @Property String name();
    }

    public WrapHttpCookie(Environment env, Cookie wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpCookie(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String name, String value) {
        __wrappedObject = new Cookie(name, value);
    }
}
