package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Arg;
import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(JsoupExtension.NAMESPACE + "Jsoup")
final public class WrapJsoup extends BaseObject {
    public WrapJsoup(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Signature
    public static Connection connect(String url) {
        return Jsoup.connect(url);
    }
}
