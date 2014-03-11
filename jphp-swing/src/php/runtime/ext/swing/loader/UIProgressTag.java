package php.runtime.ext.swing.loader;

import org.w3c.dom.Node;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.ElementItem;
import php.runtime.ext.swing.loader.support.Tag;

import javax.swing.*;

@Tag("ui-progress")
public class UIProgressTag extends BaseTag<JProgressBar> {
    @Override
    public JProgressBar create(ElementItem element, UIReader uiReader) {
        return new JProgressBar();
    }

    @Override
    public void read(ElementItem element, JProgressBar component, Node node) {
        if (isCDataContent(node))
            component.setString(node.getTextContent());
    }
}
