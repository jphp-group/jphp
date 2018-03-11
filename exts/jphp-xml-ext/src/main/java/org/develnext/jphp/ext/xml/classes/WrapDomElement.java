package org.develnext.jphp.ext.xml.classes;

import org.develnext.jphp.ext.xml.XmlExtension;
import org.w3c.dom.*;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Abstract
@Name(XmlExtension.NAMESPACE + "DomElement")
@WrapInterface(WrapDomElement.Methods.class)
public class WrapDomElement extends WrapDomNode {
    interface Methods {
        String getTagName();
        String getAttribute(String name);
        boolean hasAttribute(String name);
        boolean hasAttributeNS(String namespaceURI, String localName);
        void setAttribute(String name, String value);
        void removeAttribute(String name);

        NodeList getElementsByTagName(String name);
        NodeList getElementsByTagNameNS(String namespaceURI, String localName);

        String getAttributeNS(String namespaceURI, String localName);
        void setAttributeNS(String namespaceURI, String qualifiedName, String value);
        void removeAttributeNS(String namespaceURI, String localName);
        void setIdAttribute(String name, boolean isId);
        void setIdAttributeNS(String namespaceURI, String localName, boolean isId);
    }

    public WrapDomElement(Environment env, Element wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapDomElement(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Element getWrappedObject() {
        return (Element) super.getWrappedObject();
    }

    @Signature
    public Map<String, String> getSchemaTypeInfo(Environment env) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("name", getWrappedObject().getSchemaTypeInfo().getTypeName());
        result.put("namespace", getWrappedObject().getSchemaTypeInfo().getTypeNamespace());

        return result;
    }

    @Signature
    public void setAttributes(ForeachIterator iterator) {
        while (iterator.next()) {
            getWrappedObject().setAttribute(iterator.getKey().toString(), iterator.getValue().toString());
        }
    }

    @Signature
    public ArrayMemory getAttributes() {
        NamedNodeMap attributes = getWrappedObject().getAttributes();

        ArrayMemory result = new ArrayMemory();

        for (int i = 0; i < attributes.getLength(); i++) {
            Attr item = (Attr) attributes.item(i);

            result.putAsKeyString(item.getName(), StringMemory.valueOf(item.getValue()));
        }

        return result.toConstant();
    }

    @Signature
    public String __get(String name) {
        return getWrappedObject().getAttribute(name);
    }

    @Signature
    public void __set(String name, String value) {
        getWrappedObject().setAttribute(name, value);
    }

    @Signature
    public void __unset(String name) {
        getWrappedObject().removeAttribute(name);
    }

    @Signature
    public boolean __isset(String name) {
        return getWrappedObject().hasAttribute(name);
    }
}
