package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.Name;

@Name(JsoupExtension.NAMESPACE + "Document")
@Reflection.WrapInterface(WrapDocument.Methods.class)
public class WrapDocument extends BaseWrapper<Document> {
    public WrapDocument(Environment env, Document object) {
        super(env, object);
    }

    public WrapDocument(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public interface Methods {
        String location();

        Element head();
        Element body();

        String title();
        void title(String title);

        Document normalise();

        String outerHtml();

        Element text(String text);
        String nodeName();

        Element clone();
    }
}
