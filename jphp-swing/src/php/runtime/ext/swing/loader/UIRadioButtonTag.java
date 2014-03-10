package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

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
