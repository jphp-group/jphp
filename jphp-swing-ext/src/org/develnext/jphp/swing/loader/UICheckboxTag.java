package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-checkbox")
public class UICheckboxTag extends BaseTag<JCheckBox> {
    @Override
    public JCheckBox create(ElementItem element, UIReader uiReader) {
        return new JCheckBox();
    }

    @Override
    public void read(ElementItem element, JCheckBox component, Node node, UIReader uiReader) {
        if (isCDataContent(node))
            component.setText(uiReader.translate(component, node.getTextContent()));
    }
}
