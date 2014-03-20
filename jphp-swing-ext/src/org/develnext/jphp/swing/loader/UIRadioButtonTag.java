package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-radio-button")
public class UIRadioButtonTag extends BaseTag<JRadioButton> {
    @Override
    public JRadioButton create(ElementItem element, UIReader uiReader) {
        return new JRadioButton();
    }

    @Override
    public void read(ElementItem element, JRadioButton component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
