package org.develnext.jphp.swing.loader.support;

import org.develnext.jphp.swing.loader.UIReader;
import org.w3c.dom.Node;

import java.awt.*;

abstract public class BaseTag<T extends Component> {
    abstract public T create(ElementItem element, UIReader uiReader);

    public void read(ElementItem element, T component, Node node, UIReader uiReader) { }
    public void afterRead(ElementItem element, T component, Node node, UIReader uiReader) { }

    public void onReadAttribute(ElementItem element, String property, Value value, T component, UIReader uiReader) { }

    public void addChildren(T component, Component child) {
        if (component instanceof Container)
            ((Container) component).add(child);
    }

    public void addUnknown(T component, Node node, UIReader uiReader) {

    }

    public boolean isAllowsChildren() {
        return true;
    }

    public boolean isNeedRegister() {
        return true;
    }

    public Component getContentPane(T component) {
        return component;
    }

    public Component applyProperty(String property, T component) {
        return component;
    }

    protected static boolean isCDataContent(Node node) {
        return node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE;
    }
}
