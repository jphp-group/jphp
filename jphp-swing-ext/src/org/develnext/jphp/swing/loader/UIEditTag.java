package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JTextFieldX;

@Tag("ui-edit")
public class UIEditTag extends BaseTag<JTextFieldX> {
    @Override
    public JTextFieldX create(ElementItem element, UIReader uiReader) {
        return new JTextFieldX();
    }

    @Override
    public void read(ElementItem element, JTextFieldX component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
