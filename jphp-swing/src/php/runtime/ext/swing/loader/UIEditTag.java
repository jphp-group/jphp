package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JTextFieldX;

@Tag("ui-edit")
public class UIEditTag extends BaseTag<JTextFieldX> {
    @Override
    public JTextFieldX create(ElementItem element, UIReader uiReader) {
        return new JTextFieldX();
    }

    @Override
    public void read(ElementItem element, JTextFieldX component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
