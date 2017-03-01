package org.develnext.jphp.ext.xml.classes;

import org.develnext.jphp.ext.xml.XmlExtension;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@Name(XmlExtension.NAMESPACE + "DomNode")
@Reflection.WrapInterface(WrapDomNode.Methods.class)
public class WrapDomNode extends BaseWrapper<Node> {
    interface Methods {
        String getBaseURI();
        String getNamespaceURI();
        String getLocalName();
        String getNodeName();
        String getNodeValue();
        short getNodeType();
        String getPrefix();
        String getTextContent();
        Node getFirstChild();
        Node getLastChild();
        Node getNextSibling();
        Node getPreviousSibling();
        Node getParentNode();
        boolean hasAttributes();
        boolean hasChildNodes();
        boolean isDefaultNamespace(String namespace);
        boolean isEqualNode(Node node);
        boolean isSameNode(Node node);
        boolean isSupported(String feature, String version);
        String lookupNamespaceURI(String prefix);
        String lookupPrefix(String namespaceURI);

        void normalize();
        void setTextContent(String textContent);
        void setPrefix(String prefix);
        Node cloneNode(boolean deep);
        Node appendChild(Node node);
        Node removeChild(Node oldChild);
        Node replaceChild(Node newChild, Node oldChild);
        Node insertBefore(Node newChild, Node refChild);
    }

    protected XPath xPath = null;

    public WrapDomNode(Environment env, Node wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapDomNode(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    protected XPath getXPath() {
        if (xPath == null) {
            xPath = XPathFactory.newInstance().newXPath();
        }
        return xPath;
    }

    @Signature
    private void __construct() {}

    @Signature
    public WrapDomDocument getOwnerDocument(Environment env) {
        return new WrapDomDocument(env, getWrappedObject().getOwnerDocument());
    }

    @Signature
    public String get(String expression) throws XPathExpressionException {
        return (String) getXPath().evaluate(expression, getWrappedObject(), XPathConstants.STRING);
    }

    @Signature
    public IObject find(Environment env, String expression) throws XPathExpressionException {
        Node node = (Node) getXPath().evaluate(expression, getWrappedObject(), XPathConstants.NODE);

        if (node == null) {
            return null;
        }

        if (node instanceof Element) {
            return new WrapDomElement(env, (Element) node);
        }

        return new WrapDomNode(env, node);
    }

    @Signature
    public NodeList findAll(String expression) throws XPathExpressionException {
        return (NodeList) getXPath().evaluate(expression, getWrappedObject(), XPathConstants.NODESET);
    }

    @Signature
    public NodeList getChildNodes() {
        return getWrappedObject().getChildNodes();
    }

    @Signature
    public String __toString() {
        return getWrappedObject().getTextContent();
    }

    @Signature
    public Memory toModel() {
        return toModel(getWrappedObject());
    }

    protected static Memory toModel(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return StringMemory.valueOf(node.getTextContent());
        }

        ArrayMemory result = new ArrayMemory(true);

        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);

            result.refOfIndex("@" + attr.getNodeName()).assign(attr.getNodeValue());
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node el = children.item(i);

            if (el.getNodeType() == Node.TEXT_NODE) {
                continue;
            }

            if (el.hasAttributes() || el.hasChildNodes()) {
                if (el.getChildNodes().getLength() == 1 && el.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                    ReferenceMemory one = result.getByScalar(el.getNodeName());
                    if (one != null) {
                        if (!one.isArray()) {
                            ArrayMemory tmp = new ArrayMemory();
                            tmp.refOfPush().assign(one.toValue());

                            one.assign(tmp);
                        }

                        one.refOfPush().assign(el.getFirstChild().getTextContent());
                        result.refOfIndex(el.getNodeName()).assignRef(one.toValue());
                    } else {
                        result.refOfIndex(el.getNodeName()).assign(el.getFirstChild().getTextContent());
                    }
                } else {
                    result.refOfIndex(el.getNodeName()).assign(toModel(el).toImmutable());
                }
            } else {
                result.refOfIndex(el.getNodeName()).assign(el.getTextContent());
            }
        }

        if (result.size() == 1) {
            Memory tmp = result.getByScalar("item");
            if (tmp != null) {
                if (tmp.isArray()) {
                    return tmp.toValue(ArrayMemory.class).toConstant();
                } else {
                    ArrayMemory m = new ArrayMemory();
                    m.add(tmp.toValue());

                    return m.toConstant();
                }
            }
        }

        return result.toConstant();
    }
}
