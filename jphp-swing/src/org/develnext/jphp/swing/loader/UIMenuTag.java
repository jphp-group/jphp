package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-menu")
public class UIMenuTag extends BaseTag<JMenu> {
    @Override
    public JMenu create(ElementItem element, UIReader uiReader) {
        return new JMenu();
    }

    @Override
    public void read(ElementItem element, JMenu component, Node node) {
    }

    @Override
    public void addUnknown(JMenu component, Node node) {
        String name = node.getNodeName();
        if (name.equals("text")) {
            component.setText(node.getTextContent());
        } else if (name.equals("separator")) {
            component.addSeparator();
        }
    }
}
