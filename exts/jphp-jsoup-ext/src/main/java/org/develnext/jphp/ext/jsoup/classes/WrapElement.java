package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.reflection.ClassEntity;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static php.runtime.annotation.Reflection.Name;

@Abstract
@Name("Element")
@Namespace(JsoupExtension.NS)
public class WrapElement extends BaseWrapper<Element> implements ICloneableObject<WrapElement> {
    public WrapElement(Environment env, Element object) {
        super(env, object);
    }

    public WrapElement(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public WrapElement __clone(Environment environment, TraceInfo traceInfo) {
        return new WrapElement(environment, __wrappedObject.clone());
    }

    @Signature
    public String attr(String name) {
        return getWrappedObject().attr(name);
    }

    @Signature
    public void attr(String name, String value) {
        getWrappedObject().attr(name, value);
    }

    @Signature
    public Elements select(String cssQuery) {
        return getWrappedObject().select(cssQuery);
    }

    @Signature
    public String nodeName() {
        return getWrappedObject().nodeName();
    }

    @Signature
    public String outerHtml() {
        return getWrappedObject().outerHtml();
    }

    interface WrappedInterface {

        String tagName();
        Element tagName(String tagName);

        boolean isBlock();

        String id();

        Map<String, String> dataset();

        Element parent();
        Elements parents();

        Element child(int index);
        Elements children();

        Element appendElement(String tagName);
        Element prependElement(String tagName);

        Element appendText(String text);
        Element prependText(String text);

        Element append(String html);
        Element prepend(String html);

        Element before(String html);
        Element after(String html);

        Element empty();

        Element wrap(String html);

        String cssSelector();

        Elements siblingElements();
        Element nextElementSibling();
        Element previousElementSibling();
        Element firstElementSibling();
        Element lastElementSibling();

        Integer elementSiblingIndex();

        Elements getElementsByTag(String tagName);
        Element getElementById(String id);

        Elements getElementsByClass(String className);
        Elements getElementsByAttribute(String key);
        Elements getElementsByAttributeStarting(String keyPrefix);
        Elements getElementsByAttributeValue(String key, String value);
        Elements getElementsByAttributeValueNot(String key, String value);
        Elements getElementsByAttributeValueStarting(String key, String valuePrefix);
        Elements getElementsByAttributeValueEnding(String key, String valueSuffix);
        Elements getElementsByAttributeValueContaining(String key, String match);
        Elements getElementsByAttributeValueMatching(String key, Pattern pattern);

        Elements getElementsContainingText(String searchText);
        Elements getElementsContainingOwnText(String searchText);
        Elements getElementsMatchingText(Pattern pattern);
        Elements getElementsMatchingOwnText(Pattern pattern);

        Elements getAllElements();

        String text();
        Element text(String text);
        boolean hasText();

        String ownText();

        String data();
        String className();

        Set<String> classNames();
        Element classNames(Set<String> classNames);

        boolean hasClass(String className);
        Element addClass(String className);
        Element removeClass(String className);
        Element toggleClass(String className);

        String val();
        Element val(String value);

        String html();
        Element html(String html);
    }
}
