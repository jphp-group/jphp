package org.develnext.jphp.swing.loader.support;

import org.w3c.dom.Node;

public class ElementItem {
    protected final Node element;

    public ElementItem(Node element) {
        this.element = element;
    }

    public Node getElement() {
        return element;
    }

    public String getAttr(String name) {
        Node node = element.getAttributes().getNamedItem(name);
        if (node == null)
            return null;

        return node.getNodeValue();
    }
}
