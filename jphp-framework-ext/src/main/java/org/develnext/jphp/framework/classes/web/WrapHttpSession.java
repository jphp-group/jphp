package org.develnext.jphp.framework.classes.web;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.spl.ArrayAccess;
import php.runtime.lang.spl.Countable;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;

@Abstract
@Name(FrameworkExtension.NS + "web\\HttpSession")
public class WrapHttpSession extends BaseWrapper<HttpSession> implements ArrayAccess {
    interface WrappedInterface {
        @Property String id();
        @Property long creationTime();
        @Property long lastAccessedTime();

        @Property int maxInactiveInterval();

        void invalidate();
        boolean isNew();
    }

    public WrapHttpSession(Environment env, HttpSession wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapHttpSession(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Memory offsetExists(Environment env, Memory... args) {
        return getWrappedObject().getAttribute(args[0].toString()) != null ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public Memory offsetGet(Environment env, Memory... args) {
        Object value = getWrappedObject().getAttribute(args[0].toString());

        if (value instanceof Memory) {
            return (Memory) value;
        }

        return Memory.wrap(env, value);
    }

    @Override
    public Memory offsetSet(Environment env, Memory... args) {
        getWrappedObject().setAttribute(args[0].toString(), args[1]);
        return Memory.NULL;
    }

    @Override
    public Memory offsetUnset(Environment env, Memory... args) {
        getWrappedObject().removeAttribute(args[0].toString());
        return Memory.NULL;
    }

    public Memory toArray(Environment env) {
        ArrayMemory result = new ArrayMemory();

        Enumeration<String> names = getWrappedObject().getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = getWrappedObject().getAttribute(name);

            result.putAsKeyString(name, value instanceof Memory ? (Memory) value : Memory.wrap(env, value));
        }

        return result.toConstant();
    }
}
