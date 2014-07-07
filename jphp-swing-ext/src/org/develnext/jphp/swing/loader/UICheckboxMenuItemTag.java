package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-checkbox-menu-item")
public class UICheckboxMenuItemTag extends BaseTag<JCheckBoxMenuItem> {
    @Override
    public JCheckBoxMenuItem create(ElementItem element, UIReader uiReader) {
        return new JCheckBoxMenuItem();
    }

    @Override
    public void read(ElementItem element, JCheckBoxMenuItem component, Node node, UIReader uiReader) {
    }

    @Override
    public void addUnknown(JCheckBoxMenuItem component, Node node) {
        if (node.getNodeName().equals("text")) {
            component.setText(node.getTextContent());
        }
    }
}
