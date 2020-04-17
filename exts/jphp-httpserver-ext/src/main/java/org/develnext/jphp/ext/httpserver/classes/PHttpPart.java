package org.develnext.jphp.ext.httpserver.classes;

import org.apache.commons.io.IOUtils;
import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.DataStream;
import php.runtime.ext.core.classes.stream.MemoryStream;
import php.runtime.ext.core.classes.stream.ResourceStream;
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
        return inputStream.readAllBytes();
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
