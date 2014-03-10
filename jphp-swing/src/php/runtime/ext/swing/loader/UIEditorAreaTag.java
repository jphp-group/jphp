package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.support.JEditorPaneX;

@Tag("ui-editorarea")
public class UIEditorAreaTag extends BaseTag<JEditorPaneX> {
    @Override
    public JEditorPaneX create(ElementItem element, UIReader uiReader) {
        return new JEditorPaneX();
    }

    @Override
    public void read(ElementItem element, JEditorPaneX component, Node node) {
        if (isCDataContent(node))
            component.setText(node.getTextContent());
    }
}
