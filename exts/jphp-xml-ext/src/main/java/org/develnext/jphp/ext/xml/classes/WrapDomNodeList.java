package org.develnext.jphp.ext.xml.classes;

import org.develnext.jphp.ext.xml.XmlExtension;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.ArrayAccess;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(XmlExtension.NAMESPACE + "DomNodeList")
public class WrapDomNodeList extends BaseWrapper<NodeList> implements Iterator, Countable, ArrayAccess {
    protected int index = 0;

    public WrapDomNodeList(Environment env, NodeList wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapDomNodeList(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Signature(@Arg("offset"))
    public Memory offsetExists(Environment environment, Memory... memories) {
        return getWrappedObject().item(memories[0].toInteger()) == null ? Memory.FALSE : Memory.TRUE;
    }

    @Override
    @Signature(@Arg("offset"))
    public Memory offsetGet(Environment environment, Memory... memories) {
        Node node = getWrappedObject().item(memories[0].toInteger());

        if (node == null) {
            return Memory.NULL;
        }

        if (node instanceof Element) {
            return ObjectMemory.valueOf(new WrapDomElement(environment, (Element) node));
        }

        return ObjectMemory.valueOf(new WrapDomNode(environment, node));
    }

    @Override
    @Signature({@Arg("offset"), @Arg("value")})
    public Memory offsetSet(Environment environment, Memory... memories) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Signature(@Arg("offset"))
    public Memory offsetUnset(Environment environment, Memory... memories) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Signature
    public Memory count(Environment environment, Memory... memories) {
        return LongMemory.valueOf(getWrappedObject().getLength());
    }

    @Override
    @Signature
    public Memory current(Environment environment, Memory... memories) {
        Node node = getWrappedObject().item(index);

        if (node == null) {
            return Memory.NULL;
        }

        if (node instanceof Element) {
            return ObjectMemory.valueOf(new WrapDomElement(environment, (Element) node));
        }

        return ObjectMemory.valueOf(new WrapDomNode(environment, node));
    }

    @Override
    @Signature
    public Memory key(Environment environment, Memory... memories) {
        return LongMemory.valueOf(index);
    }

    @Override
    @Signature
    public Memory next(Environment environment, Memory... memories) {
        index += 1;
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment environment, Memory... memories) {
        index = 0;
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment environment, Memory... memories) {
        return index < getWrappedObject().getLength() ? Memory.TRUE : Memory.FALSE;
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
