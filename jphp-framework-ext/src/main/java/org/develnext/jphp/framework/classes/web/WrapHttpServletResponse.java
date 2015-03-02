package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Abstract
@Name(FrameworkExtension.NS + "web\\HttpServletResponse")
public class WrapHttpServletResponse extends BaseWrapper<HttpServletResponse> {
    public WrapHttpServletResponse(Environment env, HttpServletResponse wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpServletResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Getter
    public int getStatus() {
        return getWrappedObject().getStatus();
    }

    @Setter
    public void setStatus(int value) {
        getWrappedObject().setStatus(value);
    }
}
