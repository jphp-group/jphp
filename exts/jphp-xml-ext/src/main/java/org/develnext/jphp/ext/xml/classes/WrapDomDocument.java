package org.develnext.jphp.ext.xml.classes;

import org.develnext.jphp.ext.xml.XmlExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name(XmlExtension.NAMESPACE + "DomDocument")
@WrapInterface(WrapDomDocument.Methods.class)
public class WrapDomDocument extends WrapDomNode {
    interface Methods {
        Element getDocumentElement();
        Element getElementById(String elementId);
        String getInputEncoding();
        String getXmlEncoding();

        boolean getXmlStandalone();
        void setXmlStandalone(boolean xmlStandalone);

        String getXmlVersion();
        void setXmlVersion(String xmlVersion);

        boolean getStrictErrorChecking();
        void setStrictErrorChecking(boolean strictErrorChecking);

        String getDocumentURI();
        void setDocumentURI(String documentURI);

        Element createElementNS(String namespaceURI, String qualifiedName);

        Node importNode(Node importedNode, boolean deep);
        Node adoptNode(Node source);
        Node renameNode(Node n, String namespaceURI, String qualifiedName);

        void normalizeDocument();
    }

    public WrapDomDocument(Environment env, Document wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapDomDocument(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Document getWrappedObject() {
        return (Document) super.getWrappedObject();
    }

    @Signature
    public Element createElement(String tagName) {
        return getWrappedObject().createElement(tagName);
    }

    @Signature
    public Element importElement(Element element, boolean deep) {
        return (Element) getWrappedObject().importNode(element, deep);
    }

    @Signature
    public Node createProcessingInstruction(String name, String value) {
        return getWrappedObject().createProcessingInstruction(name, value);
    }

    @Signature
    public Element createElement(String tagName, @Nullable ArrayMemory _model) {
        Element element = getWrappedObject().createElement(tagName);

        ForeachIterator model = _model.foreachIterator(false, false);
        while (model != null && model.next()) {
            String key = model.getKey().toString();
            Memory value = model.getValue();

            if (key.startsWith("@")) {
                element.setAttribute(key.substring(1), value.toString());
            } else {
                Element subElement = getWrappedObject().createElement(key);
                if (value.isArray()) {
                    for (Memory el : value.toValue(ArrayMemory.class)) {
                        Element sub = getWrappedObject().createElement("item");

                        if (el.isArray()) {
                            sub = createElement("item", el.toValue(ArrayMemory.class));
                        } else {
                            sub.setTextContent(el.toString());
                        }

                        subElement.appendChild(sub);
                    }
                } else {
                    subElement.setTextContent(value.toString());
                }

                element.appendChild(subElement);
            }
        }

        return element;
    }
}
