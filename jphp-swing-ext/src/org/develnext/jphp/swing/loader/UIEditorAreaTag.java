package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JEditorPaneX;

@Tag("ui-editorarea")
public class UIEditorAreaTag extends BaseTag<JEditorPaneX> {
    @Override
    public JEditorPaneX create(ElementItem element, UIReader uiReader) {
        return new JEditorPaneX();
    }

    @Override
    public void read(ElementItem element, JEditorPaneX component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
