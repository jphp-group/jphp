package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-label")
public class UILabelTag extends BaseTag<JLabel> {
    @Override
    public JLabel create(ElementItem element, UIReader uiReader) {
        return new JLabel();
    }

    @Override
    public void read(ElementItem element, JLabel component, Node node, UIReader uiReader) {
        if (isCDataContent(node))
            component.setText(uiReader.translate(component, node.getTextContent()));
    }
}
