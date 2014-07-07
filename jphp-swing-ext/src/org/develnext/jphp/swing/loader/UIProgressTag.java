package org.develnext.jphp.swing.loader;

import org.w3c.dom.Node;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.ElementItem;
import org.develnext.jphp.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-progress")
public class UIProgressTag extends BaseTag<JProgressBar> {
    @Override
    public JProgressBar create(ElementItem element, UIReader uiReader) {
        return new JProgressBar();
    }

    @Override
    public void read(ElementItem element, JProgressBar component, Node node, UIReader uiReader) {
        if (isCDataContent(node))
            component.setString(uiReader.translate(component, node.getTextContent()));
    }
}
