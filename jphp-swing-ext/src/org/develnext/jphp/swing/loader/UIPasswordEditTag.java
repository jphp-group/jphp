package org.develnext.jphp.swing.loader;

import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.support.JPasswordFieldX;
import org.w3c.dom.Node;

@Tag("ui-password-edit")
public class UIPasswordEditTag extends BaseTag<JPasswordFieldX> {
    @Override
    public JPasswordFieldX create(ElementItem element, UIReader uiReader) {
        return new JPasswordFieldX();
    }

    @Override
    public void read(ElementItem element, JPasswordFieldX component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
