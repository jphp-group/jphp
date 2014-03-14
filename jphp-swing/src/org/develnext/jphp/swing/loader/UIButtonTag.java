package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-button")
public class UIButtonTag extends BaseTag<JButton> {
    @Override
    public JButton create(ElementItem element, UIReader uiReader) {
        return new JButton();
    }

    @Override
    public void read(ElementItem element, JButton component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
