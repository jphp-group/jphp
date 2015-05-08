package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.web.WebEngine;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.w3c.dom.Document;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(JavaFXExtension.NAMESPACE + "UXWebEngine")
public class UXWebEngine extends BaseWrapper<WebEngine> {
    interface WrappedInterface {
        @Property Document document();
        @Property boolean javaScriptEnabled();
        @Property String location();
        @Property String title();

        @Property String userStyleSheetLocation();

        void load(String url);
        void loadContent(String content);
        void loadContent(String content, String contentType);

        void reload();
    }

    public UXWebEngine(Environment env, WebEngine wrappedObject) {
        super(env, wrappedObject);
    }

    public UXWebEngine(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory executeScript(Environment env, String script) {
        return Memory.wrap(env, getWrappedObject().executeScript(script));
    }
}
