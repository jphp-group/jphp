package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-label")
public class UILabelTag extends BaseTag<JLabel> {
    @Override
    public JLabel create(ElementItem element, UIReader uiReader) {
        return new JLabel();
    }

    @Override
    public void read(ElementItem element, JLabel component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
