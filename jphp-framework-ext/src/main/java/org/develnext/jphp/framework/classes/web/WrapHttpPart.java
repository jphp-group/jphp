package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.Collection;

@Abstract
@Name(FrameworkExtension.NS + "web\\HttpPart")
public class WrapHttpPart extends BaseWrapper<Part> {
    interface WrappedInterface {
        @Property("input") InputStream inputStream();
        @Property String contentType();
        @Property String name();
        @Property long size();

        String getHeader(String name);
        Collection<String> getHeaders(String name);
        Collection<String> getHeaderNames();
    }

    public WrapHttpPart(Environment env, Part wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpPart(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
