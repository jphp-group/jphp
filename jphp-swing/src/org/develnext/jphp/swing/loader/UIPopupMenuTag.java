package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-popup-menu")
public class UIPopupMenuTag extends BaseTag<JPopupMenu> {
    @Override
    public JPopupMenu create(ElementItem element, UIReader uiReader) {
        return new JPopupMenu();
    }

    @Override
    public void read(ElementItem element, JPopupMenu component, Node node) {
    }

    @Override
    public void addUnknown(JPopupMenu component, Node node) {
        String name = node.getNodeName();
        if (name.equals("separator")) {
            component.addSeparator();
        }
    }
}
