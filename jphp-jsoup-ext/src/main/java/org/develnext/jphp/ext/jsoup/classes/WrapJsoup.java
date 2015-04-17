package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;

import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name("Jsoup")
@Namespace(JsoupExtension.NS)
final public class WrapJsoup extends BaseObject {
    public WrapJsoup(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void  __construct() {
        // nop.
    }

    @Signature
    public static Connection connect(String url) {
        return Jsoup.connect(url);
    }

    @Signature
    public static Document parse(Environment env, Memory source, String encoding, String baseUri) throws IOException {
        InputStream is = Stream.getInputStream(env, source);

        try {
            return Jsoup.parse(is, encoding, baseUri);
        } finally {
            Stream.closeStream(env, is);
        }
    }

    @Signature
    public static Document parseText(String text) {
        return Jsoup.parse(text);
    }

    @Signature
    public static Document parseText(String text, String baseUri) {
        return Jsoup.parse(text, baseUri);
    }
}
