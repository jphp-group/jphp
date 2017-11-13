package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Abstract
@Name("Document")
@Namespace(JsoupExtension.NS)
public class WrapDocument extends WrapElement {
    public interface WrappedInterface {
        String location();

        Element head();
        Element body();

        String title();
        void title(String title);

        Document normalise();

        Element clone();
    }

    public WrapDocument(Environment env, Document object) {
        super(env, object);
    }

    public WrapDocument(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Elements select(String query) {
        return getWrappedObject().select(query);
    }
}
