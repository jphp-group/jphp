package org.develnext.jphp.ext.jsoup.classes;

import org.develnext.jphp.ext.jsoup.JsoupExtension;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Iterator;

import static php.runtime.annotation.Reflection.Name;

@Abstract
@Name("Elements")
@Namespace(JsoupExtension.NS)
public class WrapElements extends BaseWrapper<Elements> implements php.runtime.lang.spl.iterator.Iterator {
    interface WrappedInterface {
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

    protected Iterator<Element> iterator;
    protected WrapElement iteratorValue = null;
    protected int iteratorIndex = 0;

    public WrapElements(Environment env, Elements wrappedObject) {
        super(env, wrappedObject);

        iterator = wrappedObject.iterator();

        if (iterator.hasNext()) {
            iteratorValue = new WrapElement(env, iterator.next());
        }
    }

    public WrapElements(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        return ObjectMemory.valueOf(iteratorValue);
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return iteratorValue == null ? Memory.FALSE : LongMemory.valueOf(iteratorIndex);
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        if (iterator.hasNext()) {
            iteratorValue = new WrapElement(env, iterator.next());
            iteratorIndex += 1;
        } else {
            iteratorValue = null;
        }

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        iterator = getWrappedObject().iterator();
        if (iterator.hasNext()) {
            iteratorValue = new WrapElement(env, iterator.next());
        }

        iteratorIndex = 0;

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return iteratorValue != null ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }
}
