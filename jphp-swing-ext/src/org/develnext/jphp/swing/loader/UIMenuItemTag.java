package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-menu-item")
public class UIMenuItemTag extends BaseTag<JMenuItem> {
    @Override
    public JMenuItem create(ElementItem element, UIReader uiReader) {
        return new JMenuItem();
    }

    @Override
    public void read(ElementItem element, JMenuItem component, Node node, UIReader uiReader) {
    }

    @Override
    public void addUnknown(JMenuItem component, Node node) {
        if (node.getNodeName().equals("text")) {
            component.setText(node.getTextContent());
        }
    }
}
