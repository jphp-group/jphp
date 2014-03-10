package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-toggle-button")
public class UIToggleButtonTag extends BaseTag<JToggleButton> {
    @Override
    public JToggleButton create(ElementItem element, UIReader uiReader) {
        return new JToggleButton();
    }

    @Override
    public void read(ElementItem element, JToggleButton component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
