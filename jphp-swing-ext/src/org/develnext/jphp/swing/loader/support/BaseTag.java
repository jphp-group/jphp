package org.develnext.jphp.swing.loader.support;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.UIReader;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

abstract public class BaseTag<T extends Component> {

    protected final static Set<String> contentProperties = new HashSet<String>() {{
        add("foreground");
        add("background");
        add("font");
        add("focusable");
    }};

    abstract public T create(ElementItem element, UIReader uiReader);

    public void read(ElementItem element, T component, Node node) { }
    public void afterRead(ElementItem element, T component, Node node) { }

    public void addChildren(T component, Component child) {
        if (component instanceof Container)
            ((Container) component).add(child);
    }

    public void addUnknown(T component, Node node) {

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
