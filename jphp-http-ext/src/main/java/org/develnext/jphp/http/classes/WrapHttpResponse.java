package org.develnext.jphp.http.classes;

import org.apache.http.client.methods.CloseableHttpResponse;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("php\\net\\HttpResponse")
public class WrapHttpResponse extends BaseObject {
    protected CloseableHttpResponse httpResponse;

    public WrapHttpResponse(Environment env, CloseableHttpResponse httpResponse) {
        super(env);
        this.httpResponse = httpResponse;
    }

    public WrapHttpResponse(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory getStatusCode(Environment env, Memory... args) {
        return LongMemory.valueOf(httpResponse.getStatusLine().getStatusCode());
    }

    @Signature
    public Memory getContent(Environment env, Memory... args) throws IOException {
        InputStream content = httpResponse.getEntity().getContent();
        String charsetName = Charset.defaultCharset().name();
        if (httpResponse.getEntity().getContentEncoding() != null)
            charsetName = httpResponse.getEntity().getContentEncoding().getValue();

        Scanner scanner = new Scanner(
                content, charsetName
        ).useDelimiter("\\A");

        return StringMemory.valueOf(scanner.hasNext() ? scanner.next() : "");
    }
}
