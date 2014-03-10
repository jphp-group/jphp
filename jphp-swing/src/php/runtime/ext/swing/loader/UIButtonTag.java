package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

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
