package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JTextAreaX;

@Tag("ui-textarea")
public class UITextAreaTag extends BaseTag<JTextAreaX> {
    @Override
    public JTextAreaX create(ElementItem element, UIReader uiReader) {
        return new JTextAreaX();
    }

    @Override
    public void read(ElementItem element, JTextAreaX component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
