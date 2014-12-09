package org.develnext.jphp.http.classes.entity;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\HttpEntity")
public class WrapHttpEntity extends BaseObject {
    protected HttpEntity entity;

    public WrapHttpEntity(Environment env, HttpEntity entity) {
        super(env);
        this.entity = entity;
    }

    public WrapHttpEntity(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public HttpEntity getEntity() {
        return entity;
    }

    @Signature(@Arg("source"))
    public Memory __construct(Environment env, Memory... args)
            throws UnsupportedEncodingException {
        if (args[0].instanceOf(Stream.class))
            entity = new InputStreamEntity(Stream.getInputStream(env, args[0]));
        else
            entity = new StringEntity(args[0].toString());

        return Memory.NULL;
    }

    @Signature
    public Memory getContent(Environment env, Memory... args) throws IOException {
        return new ObjectMemory(new MiscStream(env, entity.getContent()));
    }

    @Signature
    public Memory getContentType(Environment env, Memory... args) {
        return StringMemory.valueOf(entity.getContentType().getValue());
    }

    @Signature
    public Memory getContentEncoding(Environment env, Memory... args) {
        return StringMemory.valueOf(entity.getContentEncoding().getValue());
    }

    @Signature
    public Memory getContentLength(Environment env, Memory... args) {
        return LongMemory.valueOf(entity.getContentLength());
    }

    @Signature
    public Memory isChunked(Environment env, Memory... args) {
        return TrueMemory.valueOf(entity.isChunked());
    }

    @Signature
    public Memory isRepeatable(Environment env, Memory... args) {
        return TrueMemory.valueOf(entity.isRepeatable());
    }

    @Signature
    public Memory isStreaming(Environment env, Memory... args) {
        return TrueMemory.valueOf(entity.isStreaming());
    }

    @Signature(@Arg(value = "encoding", optional = @Optional("null")))
    public Memory toString(Environment env, Memory... args) throws IOException {
        String charsetName = args[0].isNull() ? env.getDefaultCharset().name() : args[0].toString();
        if (args[0].isNull() && entity.getContentEncoding() != null)
            charsetName = entity.getContentEncoding().getValue();

        Scanner scanner = new Scanner(
                entity.getContent(), charsetName
        ).useDelimiter("\\A");

        return StringMemory.valueOf(scanner.hasNext() ? scanner.next() : "");
    }
}
