package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-checkbox")
public class UICheckboxTag extends BaseTag<JCheckBox> {
    @Override
    public JCheckBox create(ElementItem element, UIReader uiReader) {
        return new JCheckBox();
    }

    @Override
    public void read(ElementItem element, JCheckBox component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
