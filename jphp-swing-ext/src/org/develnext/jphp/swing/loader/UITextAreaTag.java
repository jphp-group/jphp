package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JTextAreaX;

@Tag("ui-textarea")
public class UITextAreaTag extends BaseTag<JTextAreaX> {
    @Override
    public JTextAreaX create(ElementItem element, UIReader uiReader) {
        return new JTextAreaX();
    }

    @Override
    public void read(ElementItem element, JTextAreaX component, Node node, UIReader uiReader) {
        if (isCDataContent(node))
            component.setText(uiReader.translate(component, node.getTextContent()));
    }
}
