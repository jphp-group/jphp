package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Traversable;
import php.runtime.lang.spl.iterator.IteratorAggregate;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Iterator;

import static php.runtime.annotation.Reflection.Name;

@Name(JsoupExtension.NAMESPACE + "Elements")
@Reflection.WrapInterface(WrapElements.Methods.class)
public class WrapElements extends BaseWrapper<Elements> implements Iterable<Memory> {
    public WrapElements(Environment env, Elements wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapElements(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Iterator<Memory> iterator() {
        final Iterator<Element> iterator = getWrappedObject().iterator();
        return new Iterator<Memory>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Memory next() {
                return new ObjectMemory(new WrapElement(getEnvironment(), iterator.next()));
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    interface Methods {
        String attr(String attributeKey);
        boolean hasAttr(String attributeKey);
        Elements attr(String attributeKey, String attributeValue);
        Elements removeAttr(String attributeKey);

        Elements addClass(String className);
        Elements removeClass(String className);
        Elements toggleClass(String className);
        boolean hasClass(String className);

        String val();
        Elements val(String value);

        String text();
        boolean hasText();
        String html();
        Elements html(String html);
        String outerHtml();

        Elements prepend(String html);
        Elements append(String html);
        Elements before(String html);
        Elements after(String html);
        Elements wrap(String html);
        Elements unwrap();
        Elements remove();

        Elements select(String query);
        Elements not(String query);
        boolean is(String query);

        Elements parents();

        Element first();
        Element last();
    }
}
