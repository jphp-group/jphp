package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Reflection.Name("HttpPart")
@Reflection.Namespace(HttpServerExtension.NS)
public class PHttpPart extends BaseWrapper<Part> {
    public PHttpPart(Environment env, Part wrappedObject) {
        super(env, wrappedObject);
    }

    public PHttpPart(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public byte[] readAll() throws IOException {
        ByteArrayInputStream inputStream = (ByteArrayInputStream) getWrappedObject().getInputStream();
        byte[] array = new byte[inputStream.available()];
        inputStream.read(array);
        inputStream.close();
        return array;
    }

    @Reflection.Signature
    public String getName() {
        return getWrappedObject().getName();
    }

    @Reflection.Signature
    public String getContentType() {
        return getWrappedObject().getContentType();
    }

    @Reflection.Signature
    public String getSubmittedFileName() {
        return getWrappedObject().getSubmittedFileName();
    }

    @Reflection.Signature
    public long getSize() {
        return getWrappedObject().getSize();
    }
}
