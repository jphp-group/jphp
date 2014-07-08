package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JRichTextAreaX;
import org.w3c.dom.Node;

@Tag("ui-rich-textarea")
public class UIRichTextAreaTag extends BaseTag<JRichTextAreaX> {
    @Override
    public JRichTextAreaX create(ElementItem element, UIReader uiReader) {
        return new JRichTextAreaX();
    }

    @Override
    public void read(ElementItem element, JRichTextAreaX component, Node node, UIReader uiReader) {
        if (isCDataContent(node))
            component.setText(uiReader.translate(component, node.getTextContent()));
    }
}
